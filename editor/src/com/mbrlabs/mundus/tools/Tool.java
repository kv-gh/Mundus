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

package com.mbrlabs.mundus.tools;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.mbrlabs.mundus.core.project.ProjectContext;

/**
 * @author Marcus Brummer
 * @version 25-12-2015
 */
public abstract class Tool extends InputAdapter implements Disposable {

    protected ProjectContext projectContext;
    protected ModelBatch batch;
    protected Shader shader;

    public Tool(ProjectContext projectContext, Shader shader, ModelBatch batch) {
        this.projectContext = projectContext;
        this.batch = batch;
        this.shader = shader;
    }

    public abstract String getName();
    public abstract Drawable getIcon();

    public abstract void render();
    public abstract void act();

}
