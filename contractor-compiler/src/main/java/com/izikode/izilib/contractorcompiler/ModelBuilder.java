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

class ModelBuilder extends ComponentBuilder {

    public ModelBuilder(TypeElement type, String componentPackage, String componentName) {
        super(type, componentPackage, componentName);
    }

    private PresenterBuilder presenter;

    public void setPresenter(PresenterBuilder presenter) {
        this.presenter = presenter;
    }

    @Override
    protected String referencesCode() {
        return referenceComponents(presenter);
    }
}
