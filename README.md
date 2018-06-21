# Contractor
##### Annotate your MVP Contract interface to automatically generate your component classes
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Development](https://img.shields.io/badge/Stability-Development-green.svg)](https://shields.io/) [![Bintray](https://img.shields.io/badge/Bintray-0.1-lightgrey.svg)](https://dl.bintray.com/ifanie/izilib)
## Premise
This library aims to minimize the <b>MVP</b> setup boilerplate code to the minimum. Decorate your <b>Contract</b> interfaces, build, and extend the generated abstract classes. Your contract funtions will be there, as well as all needed references. The <b>MODEL</b> and the <b>VIEW</b> will contain references to the <b>PRESENTER</b>, and the <b>PRESENTER</b> to both the <b>MODEL</b> and the <b>VIEW</b>. Enhance further by setting Base classes which will be extended by the generated abstract classes.
### Install
```java
implementation 'com.izikode.izilib:contractor:0.1'
annotationProcessor 'com.izikode.izilib:contractor:0.1'
```
##### - or with kotlin
```kotlin
implementation 'com.izikode.izilib:contractor:0.1'
kapt 'com.izikode.izilib:contractor:0.1'
```
### Sample usage
#### a. Create and decorate yout contract
```kotlin
@Contract
interface MainContract {}
```
#### b. Create and decorate your component interfaces
```kotlin
@Contract
interface MainContract {

    @Contract.Model
    interface Model {}

    @Contract.View
    interface View {}

    @Contract.Presenter
    interface Presenter {}

}
```
#### c. Write your functions and <i>BUILD</i>
#### d. Extend the generated classes for your final implementation
```kotlin
class MainPresenter : AbstractMainPresenter() {}
```
##### - Naming convention
interface <b>MainContract</b> PLUS interface <i>Model</i> GENERATES Abstract<b>Main</b><i>Model</i>
#### e. Initialize the Objects and References
```kotlin
val model = MainModel()
val view = MainView()
val presenter = MainPresenter()

presenter.setup(model).setup(view)
```
##### - Access the references simply through
``` model() ```
``` view() ```
``` presenter() ```
#### f. Optionally, set a target build package
``` @Contract(target = "com.somepackage") ```
#### g. Optionally, have your components extend BASE classes
```kotlin
@Contract.Model(base = BaseModel::class)
interface Model
```
## TODO
- Use javapoet for code generation
- Finilize logic and implementation for the Contractor class (possibly creating objects and setup automatically)
## Licence
```licence
Copyright 2018 Fanie Veizis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
