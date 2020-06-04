package component

import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.*
import org.w3c.dom.events.Event
import react.*
import react.dom.*

interface addLessonProps :RProps{
    var onAdd : (Event) -> Unit
}

fun RBuilder.fAddLesson( addLesson :(Event)->Unit) =
    child(functionalComponent<addLessonProps> {props ->
        input(InputType.text) {
            attrs { id = "Lessons" } }
            button {
            +"Add"
            attrs.onClickFunction = props.onAdd
        }
    }){
        attrs.onAdd = addLesson
    }