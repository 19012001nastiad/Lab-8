package component

import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.button
import react.dom.input
import react.functionalComponent

interface addStudentProps : RProps {
    var onAdd : (Event) -> Unit
}
fun RBuilder.fAddStudent (addStudent : (Event)->Unit) =
    child(functionalComponent<addStudentProps> { props ->
        input(InputType.text) {
            attrs { id = "Students" } }
            button {
            +"Add"
            attrs.onClickFunction = props.onAdd
        }
    }){
        attrs.onAdd = addStudent
    }