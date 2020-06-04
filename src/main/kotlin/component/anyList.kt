package component

import hoc.withDisplayName
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.functionalComponent
import react.router.dom.navLink

interface AnyListProps<O> : RProps {
    var objs: Array<O>
}

fun <T> fAnyList(name: String, path: String) =
    functionalComponent<AnyListProps<T>> {props ->
        h2 { +name }
        ul {
            props.objs.mapIndexed{ index, obj ->
                li {
                    navLink("$path/$index"){
                        +obj.toString()
                    }
                }
            }
        }
    }

fun <T> RBuilder.anyList(
    anys: Array<T>,
    name: String,
    path: String
) = child(
    withDisplayName(name, fAnyList<T>(name, path))
){
    attrs.objs = anys
}

