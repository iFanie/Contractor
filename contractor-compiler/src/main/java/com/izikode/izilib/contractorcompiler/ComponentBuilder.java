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

import javax.lang.model.element.TypeElement;

abstract class ComponentBuilder {

    public final TypeElement type;

    protected final String componentPackage;
    protected final String componentName;

    ComponentBuilder(TypeElement type, String componentPackage, String componentName) {
        this.type = type;

        this.componentPackage = componentPackage;
        this.componentName = componentName;
    }

    protected String contractPackage;
    protected String contractClass;
    protected String contractName;

    public void from(ContractorBuilder contractor) {
        contractPackage = contractor.packageName;
        contractClass = contractor.contractName;
        contractName = contractor.name();
    }

    protected String baseClass;

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public String referenceName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("builder", "");
    }

    private String implementationName() {
        return String.format("Abstract%1$s%2$s", contractName, componentName);
    }

    public String fileName() {
        return String.format("%1$s.%2$s", componentPackage, implementationName());
    }

    private String interfaceName() {
        return String.format("%1$s.%2$s.%3$s", contractPackage, contractClass, componentName);
    }

    private static final String NL = "\r\n";
    private static final String TB = "\t";

    public String sourceCode() {
        return (new StringBuilder()

                .append(packageCode()).append(NL)
                .append(NL)
                .append(classHeaderCode()).append(NL)
                .append(NL)
                .append(referencesCode()).append(NL)
                .append(NL)
                .append(classFooterCode())
                .append(NL)

        ).toString();
    }

    protected String packageCode() {
        return String.format("package %1$s;", componentPackage);
    }

    protected String classHeaderCode() {
        if (baseClass == null) {
            return String.format("public abstract class %1$s implements %2$s {",
                    implementationName(), interfaceName());
        } else {
            return String.format("public abstract class %1$s extends %2$s implements %3$s {",
                    implementationName(), baseClass, interfaceName());
        }
    }

    protected abstract String referencesCode();

    protected String classFooterCode() {
        return "}";
    }

    protected String referenceComponents(boolean cyclic, ComponentBuilder ... components) {
        StringBuilder stringBuilder = new StringBuilder();

        int index = 0;
        for (ComponentBuilder component : components) {
            stringBuilder.append(referenceComponent(component, cyclic));

            if (index < (components.length - 1)) {
                stringBuilder
                        .append(NL)
                        .append(NL);
            }

            index++;
        }

        return stringBuilder.toString();
    }

    protected String referenceComponents(ComponentBuilder ... components) {
        return referenceComponents(false, components);
    }

    private String referenceComponent(ComponentBuilder component, boolean cyclic) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(TB).append(String.format("private %1$s %2$s;", component.fileName(), component.referenceName())).append(NL)
                .append(NL)
                .append(TB).append(String.format("public %1$s setup(%2$s %3$s) {", fileName(), component.fileName(), component.referenceName())).append(NL)
                .append(TB).append(TB).append(String.format("this.%1$s = %1$s;", component.referenceName())).append(NL);

        if (cyclic) {
            stringBuilder
                    .append(TB).append(TB).append(String.format("this.%1$s.setup(this);", component.referenceName())).append(NL)
                    .append(NL);
        }

        stringBuilder
                .append(TB).append(TB).append("return this;").append(NL)
                .append(TB).append("}").append(NL)
                .append(NL)
                .append(TB).append(String.format("public %1$s %2$s() {", component.fileName(), component.referenceName())).append(NL)
                .append(TB).append(TB).append(String.format("return %1$s;", component.referenceName())).append(NL)
                .append(TB).append("}");

        return  stringBuilder.toString();
    }

}
