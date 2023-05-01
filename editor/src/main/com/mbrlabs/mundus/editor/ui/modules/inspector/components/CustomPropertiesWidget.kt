package com.mbrlabs.mundus.editor.ui.modules.inspector.components

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.ui.modules.inspector.BaseInspectorWidget
import com.mbrlabs.mundus.editor.ui.widgets.FaTextButton
import com.mbrlabs.mundus.editor.utils.Fa

class CustomPropertiesWidget : BaseInspectorWidget("Custom Properties") {

    private val customProperties = VisTable()
    private var gameObject: GameObject? = null

    init {
        isDeletable = false

        setupUI()
    }
    override fun onDelete() {
        // The custom properties component can't be deleted.
    }

    override fun setValues(go: GameObject) {
        this.gameObject = go
        val goCustomProperties = go.customProperties

        customProperties.clearChildren()

        for (entry in goCustomProperties) {
            addCustomProperty(entry.key, entry.value)
        }
    }

    private fun setupUI() {
        collapsibleContent.add(customProperties).row()

        val addButton = VisTextButton("Add")
        collapsibleContent.add(addButton)

        addButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val key = ""
                val value = ""
                gameObject!!.customProperties.put(key, value)
                addCustomProperty(key, value)
            }
        })
    }

    private fun addCustomProperty(key: String, value: String) {
        var previousKey = key

        val keyTextField = VisTextField(key)
        val valueTextField = VisTextField(value)
        val deleteButton = FaTextButton(Fa.TIMES)

        customProperties.add(keyTextField).padBottom(3f).padRight(3f)
        customProperties.add(valueTextField).padBottom(3f).padRight(3f)
        customProperties.add(deleteButton).row()

        keyTextField.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val currentKey = keyTextField.text

                val goCustomProperties = gameObject!!.customProperties

                goCustomProperties.remove(previousKey)
                goCustomProperties.put(currentKey, valueTextField.text)

                previousKey = currentKey
            }
        })

        valueTextField.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val currentKey = keyTextField.text
                val currentValue = valueTextField.text

                val goCustomProperties = gameObject!!.customProperties

                goCustomProperties.put(currentKey, currentValue)
            }
        })
    }
}
