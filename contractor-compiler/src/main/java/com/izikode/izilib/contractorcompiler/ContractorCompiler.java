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

import com.izikode.izilib.contractorannotations.Contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public class ContractorCompiler extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                Contract.class.getCanonicalName(),
                Contract.Model.class.getCanonicalName(),
                Contract.View.class.getCanonicalName(),
                Contract.Presenter.class.getCanonicalName()
        ));
    }

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Collection<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Contract.class);

        if (elements.size() < 1) {
            return false;
        }

        List<TypeElement> types = ElementFilter.typesIn(elements);

        if (types.size() < 1) {
            return false;
        }

        boolean notConsumed = false;

        for (TypeElement type : types) {
            PackageElement packageElement = (PackageElement) type.getEnclosingElement();
            String contractPackage = packageElement.getQualifiedName().toString();

            String targetPackage = type.getAnnotation(Contract.class).target();

            if (targetPackage.trim().equals("")) {
                targetPackage = contractPackage;
            }

            String contractClassName = type.getSimpleName().toString();
            ContractorBuilder contractor = new ContractorBuilder(filer, messager, contractPackage, contractClassName);

            List<? extends Element> members = elementUtils.getAllMembers(type);
            List<TypeElement> nestedTypes = ElementFilter.typesIn(members);

            for (TypeElement nestedType :nestedTypes) {
                ComponentBuilder component = null;

                Class baseClass = null;
                TypeMirror baseClassMirror = null;

                String className = nestedType.getSimpleName().toString();

                if (nestedType.getAnnotation(Contract.Model.class) != null) {
                    component = new ModelBuilder(nestedType, targetPackage, className);

                    try {
                        baseClass = nestedType.getAnnotation(Contract.Model.class).base();
                    } catch (MirroredTypeException exception) {
                        baseClassMirror = exception.getTypeMirror();
                    }

                    contractor.setModel((ModelBuilder) component);
                } else if (nestedType.getAnnotation(Contract.View.class) != null) {
                    component = new ViewBuilder(nestedType, targetPackage, className);

                    try {
                        baseClass = nestedType.getAnnotation(Contract.View.class).base();
                    } catch (MirroredTypeException exception) {
                        baseClassMirror = exception.getTypeMirror();
                    }

                    contractor.setView((ViewBuilder) component);
                } else if (nestedType.getAnnotation(Contract.Presenter.class) != null) {
                    component = new PresenterBuilder(nestedType, targetPackage, className);

                    try {
                        baseClass = nestedType.getAnnotation(Contract.Presenter.class).base();
                    } catch (MirroredTypeException exception) {
                        baseClassMirror = exception.getTypeMirror();
                    }

                    contractor.setPresenter((PresenterBuilder) component);
                }

                if (component != null && (baseClass != null || baseClassMirror != null)) {
                    if (baseClass != null && !baseClass.equals(Void.class)) {
                        component.setBaseClass(baseClass.getCanonicalName());
                    } else if (baseClassMirror != null) {
                        String baseClassName = baseClassMirror.toString().trim();

                        if (!baseClassName.equals("") && !baseClassName.equals(Void.class.getCanonicalName())) {
                            component.setBaseClass(baseClassName);
                        }
                    }
                }
            }

            contractor.setReferences();
            notConsumed = !contractor.writeSourceFiles();
        }

        return notConsumed;
    }

    private void info(String string) {
        messager.printMessage(Diagnostic.Kind.NOTE, string);
    }

}
