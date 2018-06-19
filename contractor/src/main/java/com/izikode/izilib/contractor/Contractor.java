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

package com.izikode.izilib.contractor;

class Contractor<Model, View, Presenter> {

    /* TODO implement so it actually does the SETUP function automatically */

    private Class contract;

    public Contractor contract(Class contract) {
        this.contract = contract;
        return this;
    }

    private Model model;
    private View view;
    private Presenter presenter;

    public Contractor components(Model model, View view, Presenter presenter) {
        this.model = model;
        this.view = view;
        this.presenter = presenter;

        return this;
    }

}
