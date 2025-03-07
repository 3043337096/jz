/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruifenglb.www.ui.down.cache;

import android.view.View;

/**
 * @author drakeet
 */
public class Square {

    public final String number;
    public boolean isSelected;
    public boolean finished;
    public View.OnClickListener clickListener;


    public Square(String number, View.OnClickListener clickListener) {
        this.number = number;
        this.clickListener = clickListener;
    }
}
