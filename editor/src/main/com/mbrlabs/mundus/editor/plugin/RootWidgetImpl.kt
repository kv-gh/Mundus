package com.mbrlabs.mundus.editor.plugin

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisRadioButton
import com.kotcrab.vis.ui.widget.VisSelectBox
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.SimpleFloatSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.Spinner
import com.kotcrab.vis.ui.widget.spinner.SpinnerModel
import com.mbrlabs.mundus.pluginapi.ui.ButtonListener
import com.mbrlabs.mundus.pluginapi.ui.CheckboxListener
import com.mbrlabs.mundus.pluginapi.ui.FloatSpinnerListener
import com.mbrlabs.mundus.pluginapi.ui.RadioButtonListener
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.IntSpinnerListener
import com.mbrlabs.mundus.pluginapi.ui.SpinnerListener
import com.mbrlabs.mundus.pluginapi.ui.Cell
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell
import com.mbrlabs.mundus.pluginapi.ui.SelectBoxListener

class RootWidgetImpl : VisTable(), RootWidget {

    override fun addLabel(text: String): Cell {
        val label = VisLabel(text)
        val cell = add(label)
        return CellImpl(cell)
    }

    override fun addTextButton(text: String, listener: ButtonListener): Cell {
        val button = VisTextButton(text)
        val cell = add(button)
        return CellImpl(cell)
    }

    override fun addRadioButtons(button1Text: String, button2Text: String, listener: RadioButtonListener) : Cell {
        return addRadioButtons(button1Text, button2Text, true, listener)
    }

    override fun addRadioButtons(button1Text: String, button2Text: String, selectedFirst: Boolean, listener: RadioButtonListener): Cell {
        var selectedButtonText = if (selectedFirst) button1Text else button2Text

        val radioButton1 = VisRadioButton(button1Text)
        radioButton1.isChecked = selectedFirst

        val radioButton2 = VisRadioButton(button2Text)
        radioButton2.isChecked = !selectedFirst

        radioButton1.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (selectedButtonText != button1Text) {
                    listener.selected(button1Text)
                    selectedButtonText = button1Text
                    radioButton2.isChecked = false
                } else {
                    radioButton1.isChecked = true
                }
            }
        })

        radioButton2.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (selectedButtonText != button2Text) {
                    listener.selected(button2Text)
                    selectedButtonText = button2Text
                    radioButton1.isChecked = false
                } else {
                    radioButton2.isChecked = true
                }
            }
        })

        val cell1 = add(radioButton1)
        val cell2 = add(radioButton2)
        return CellImpl(cell1, cell2)
    }

    override fun addSpinner(text: String, min: Int, max: Int, initValue: Int, listener: IntSpinnerListener): Cell {
        return addSpinner(text, min, max, initValue, 1, listener)
    }

    override fun addSpinner(text: String, min: Int, max: Int, initValue: Int, step: Int, listener: IntSpinnerListener) : Cell {
        val spinnerModel = IntSpinnerModel(initValue, min, max, step)
        return addSpinner(text, spinnerModel, listener) { spinnerModel.value }
    }

    override fun addSpinner(text: String, min: Float, max: Float, initValue: Float, listener: FloatSpinnerListener): Cell {
        return addSpinner(text, min, max, initValue, 1f, listener)
    }

    override fun addSpinner(text: String, min: Float, max: Float, initValue: Float, step: Float, listener: FloatSpinnerListener): Cell {
        val spinnerModel = SimpleFloatSpinnerModel(initValue, min, max, step)
        return addSpinner(text, spinnerModel, listener) { spinnerModel.value }
    }

    override fun addCheckbox(text: String, listener: CheckboxListener) : Cell {
        return addCheckbox(text, false, listener)
    }

    override fun addCheckbox(text: String, checked: Boolean, listener: CheckboxListener): Cell {
        val checkbox = VisCheckBox(text)
        checkbox.isChecked = checked
        checkbox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                listener.changed(checkbox.isChecked)
            }
        })

        val cell = add(checkbox)
        return CellImpl(cell)
    }

    override fun <T> addSelectBox(selectList: Array<T>, listener: SelectBoxListener<T>): Cell {
        return addSelectBox(selectList, selectList.first(), listener)
    }

    override fun <T : Any?> addSelectBox(selectList: Array<T>, defaultValue: T, listener: SelectBoxListener<T>): Cell {
        val selectBox = VisSelectBox<T>()
        selectBox.items = selectList
        selectBox.selected = defaultValue
        selectBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                listener.selected(selectBox.selected)
            }
        })

        val cell = add(selectBox)
        return CellImpl(cell)
    }

    override fun addRow() {
        row()
    }

    override fun addEmptyWidget(): RootWidgetCell {
        val emptyWidget = RootWidgetImpl()

        val cell = add(emptyWidget)
        return RootWidgetCellImpl(cell)
    }

    override fun clearWidgets() {
        clear()
    }

    private fun <T> addSpinner(text: String, spinnerModel: SpinnerModel, listener: SpinnerListener<T>, getModelValue: () -> T): Cell {
        val spinner = Spinner(text, spinnerModel)
        spinner.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                listener.changed(getModelValue())
            }
        })

        val cell = add(spinner)
        return CellImpl(cell)
    }
}
