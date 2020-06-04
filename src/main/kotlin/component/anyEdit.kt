package component

import kotlinx.html.js.onClickFunction
import kotlinx.html.onClick
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import kotlin.browser.document

interface anyEditProps<O> :RProps{
    var subObj : Array<O>
    var onAdd : (Event) -> Unit
    var onDelete : (Int) -> Unit
    var name : String
    var path : String
}
fun <O> fanyEdit(
    rAddObj :RBuilder.((Event)->Unit) -> ReactElement,
    rComponent: RBuilder.(Array<O>, String, String)->ReactElement
) =
    functionalComponent<anyEditProps<O>> {props ->
    h2{+"Редактирование списка"}
        p {
            +"Редактирование ${props.name} здесь:"
            br{}
            rAddObj(props.onAdd)
            props.subObj.forEachIndexed {index,obj ->
                li {
                    span {
                        +"Delete $obj"
                        attrs.onClickFunction = { props.onDelete(index) }
                    }
                }
            }
            rComponent(props.subObj,props.name,props.path)
        }
}

fun <O> RBuilder.anyEdit(
    rComponent: RBuilder.(Array<O>, String, String)->ReactElement,
    rAddObj :RBuilder.((Event)->Unit) -> ReactElement,
    subObj : Array<O>,
    onAdd: (Event) -> Unit,
    onDelete: (Int) -> Unit,
    name : String,
    path: String
    ) = child(fanyEdit<O>(rAddObj,rComponent)){
    attrs.onAdd=onAdd
    attrs.subObj=subObj
    attrs.onDelete = onDelete
    attrs.name = name
    attrs.path = path
}