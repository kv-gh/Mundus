/*
 * Copyright (c) 2015. See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mbrlabs.mundus.ui.handler;

import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.UBJsonReader;
import com.mbrlabs.mundus.utils.FbxConv;
import com.mbrlabs.mundus.ui.Ui;

/**
 * @author Marcus Brummer
 * @version 28-11-2015
 */
public class ModelImportHandler extends ChangeListener {

    private FbxConv fbxConv;

    private G3dModelLoader g3dbLoader;

    public ModelImportHandler() {
        fbxConv = new FbxConv();
        g3dbLoader = new G3dModelLoader(new UBJsonReader());
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        final Ui ui = Ui.getInstance();
        ui.showDialog(ui.getImportModelDialog());
    }

}
