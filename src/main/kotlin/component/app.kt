package component

import data.*
import hoc.withDisplayName
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import kotlin.browser.document
import kotlin.reflect.KClass

interface AppProps : RProps {}

interface AppState : RState {
    var lessons: Array<Lesson>
    var students: Array<Student>
    var presents: Array<Array<Boolean>>
}

interface RouteNumberResult : RProps {
    var number: String
}

class App : RComponent<AppProps, AppState>() {
    override fun componentWillMount() {
        state.lessons = lessonsList
        state.students = studentList
        state.presents = Array(state.lessons.size) {
            Array(state.students.size) { false }
        }

    }

    override fun RBuilder.render() {
        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li {navLink("/editstudents"){+"Edit students"} }
                    li {navLink("/editlessons"){+"Edit lessons"} }
                }
            }
        }

        switch {
            route("/lessons",
                exact = true,
                render = {
                    anyList(state.lessons, "Lessons", "/lessons")
                }
            )
            route("/students",
                exact = true,
                render = {
                    anyList(state.students, "Students", "/students")
                }
            )
            route("/lessons/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val lesson = state.lessons.getOrNull(num)
                    if (lesson != null)
                        anyFull(
                            RBuilder::student,
                            lesson,
                            state.students,
                            state.presents[num]
                        ) { onClick(num, it) }
                    else
                        p { +"No such lesson" }
                }
            )
            route("/students/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val student = state.students.getOrNull(num)
                    if (student != null)
                        anyFull(
                            RBuilder::lesson,
                            student,
                            state.lessons,
                            state.presents.map {
                                it[num]
                            }.toTypedArray()
                        ) { onClick(it, num) }
                    else
                        p { +"No such student" }
                }
            )
            route("/editstudents",
            render = {
                anyEdit(
                    RBuilder::anyList,
                    RBuilder::fAddStudent,
                    state.students,
                    addNewStudent(),
                    deleteOldStudent(),
                    "Students",
                    "/students"
                )
            })
            route("/editlessons",
            render = {
                anyEdit(
                    RBuilder::anyList,
                    RBuilder::fAddLesson,
                    state.lessons,
                    addNewLesson(),
                    deleteOldLesson(),
                    "Lessons",
                    "/lessons"
                )
            })
        }
    }

    fun onClick(indexLesson: Int, indexStudent: Int) =
        { _: Event ->
            setState {
                presents[indexLesson][indexStudent] =
                    !presents[indexLesson][indexStudent]
            }
        }

    fun addNewLesson():(Event) -> Unit = {
        val nameObj = document.getElementById("Lessons") as HTMLInputElement
        val editedLesson = state.presents.mapIndexed { index, _ ->
            state.presents[index].plus(arrayOf(false))
        }.toTypedArray()
        setState {
            lessons += Lesson(nameObj.value)
            presents = editedLesson
        }
    }

    fun deleteOldLesson() :(Int) -> Unit = {
        val editedLessons = state.lessons.toMutableList().apply {
            removeAt(it) }
            .toTypedArray()
        val editedPresents = state.presents.mapIndexed { index, _ ->
            state.presents[index].toMutableList().apply {
                removeAt(it)
            }.toTypedArray()
        }.toTypedArray()
        setState{
            lessons = editedLessons
            presents= editedPresents
        }
    }

    fun addNewStudent():(Event) -> Unit = {
        val nameObj = document.getElementById("Students") as HTMLInputElement
        val newStr = nameObj.value.split(" ")
        setState {
            students += Student(newStr[0],newStr[1])
            presents += arrayOf(Array(state.students.size){false})
        }
    }

    fun deleteOldStudent() :(Int) -> Unit = {
        val editedStudents = state.students.toMutableList().apply {
            removeAt(it) }
            .toTypedArray()
        val editedPresents = state.presents.toMutableList().apply {
            removeAt(it)}
            .toTypedArray()
        setState{
            students = editedStudents
            presents= editedPresents
        }
    }
}

fun RBuilder.app() =
    child(
        withDisplayName("AppHoc", App::class)
    ) {

    }





