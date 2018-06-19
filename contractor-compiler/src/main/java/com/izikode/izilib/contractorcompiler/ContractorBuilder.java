/*
 * Copyright 2018 Fanie Veizis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.izikode.izilib.contractorcompiler;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

class ContractorBuilder {

    private final Filer filer;
    private final Messager messager;

    public final String packageName;
    public final String contractName;

    public ContractorBuilder(Filer filer, Messager messager, String packageName, String contractName) {
        this.filer = filer;
        this.messager = messager;

        this.packageName = packageName;
        this.contractName = contractName;
    }

    private ModelBuilder model;

    public void setModel(ModelBuilder model) {
        model.from(this);
        this.model = model;
    }

    private ViewBuilder view;

    public void setView(ViewBuilder view) {
        view.from(this);
        this.view = view;
    }

    private PresenterBuilder presenter;

    public void setPresenter(PresenterBuilder presenter) {
        presenter.from(this);
        this.presenter = presenter;
    }

    public String name() {
        if (contractName.endsWith("Contract")) {
            return contractName.replaceAll("Contract", "");
        }

        if (contractName.endsWith("Contractor")) {
            return contractName.replaceAll("Contractor", "");
        }

        return contractName;
    }

    private String fileName() {
        return String.format("%1$s.%2$s", packageName, contractorName());
    }

    private String contractorName() {
        if (contractName.endsWith("Contractor")) {
            return contractName;
        }

        if (contractName.endsWith("Contract")) {
            return String.format("%1$sor", contractName);
        }

        return String.format("%1$sContractor", contractName);
    }

    public void setReferences() {
        model.setPresenter(presenter);
        view.setPresenter(presenter);

        presenter.setModelAndView(model, view);
    }

    public boolean writeSourceFiles() {
        for (ComponentBuilder component : new ComponentBuilder[] { model, view, presenter }) {
            if (!writeClass(component.type, component.fileName(), component.sourceCode())) {
                return false;
            }
        }

        return true;
    }

    private void info(String string) {
        messager.printMessage(Diagnostic.Kind.NOTE, string);
    }

    private void error(Exception exception, TypeElement type) {
        messager.printMessage(Diagnostic.Kind.ERROR, exception.toString(), type);
    }

    private boolean writeClass(TypeElement type, String fileName, String sourceCode) {
        try {

            JavaFileObject fileObject = filer.createSourceFile(fileName);
            Writer writer = fileObject.openWriter();

            writer.write(sourceCode);
            writer.close();

            return true;

        } catch (IOException exception) {

            error(exception, type);
            return false;

        }
    }

}
