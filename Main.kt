/**
 * Universidad Simón Bolívar.
 *
 * Descripción: Programa cliente interactivo que ejecuta la implementación
 * concreta del TAD AIFA, es decir, un administrador de información académica
 * que contiene los datos de estudiantes, asignaturas y sus calificaciones.
 * 
 * @author Ka Fung <18-10492@usb.ve>
 */

import java.io.File

/** Imprime el menú de opciones que puede realizar el usuario. */
fun menuOpciones() {
    println(" \n MENÚ DE OPCIONES A EFECTUAR:\n")
    println(" 1. Crear un administrador vacío.")
    println(" 2. Agregar información académica.")
    println(" 3. Eliminar información académica.")
    println(" 4. Listar materias/calificaciones de un estudiante dado.")
    println(" 5. Listar estudiantes/calificaciones de una materia dada.")
    println(" 6. Cargar un administrador académico completo desde un archivo dado.")
    println(" 7. Listar toda la información contenida en el administrador académico")
    println("    ordenada por estudiante/materia.")
    println(" 8. Ejecutar la función sorpresita.")
    println(" 9. Salir del programa.")
}

/** 
 * Pregunta al cliente si desea crear un nuevo administrador académico, 
 * en caso de que ya exista uno creado.
 *
 * @return true si se ha creado un nuevo administrador, en cambio, retorna false. 
 */
fun menuCrear(): Boolean {
    val si = arrayOf("si", "Si", "SI", "sí", "Sí", "SÍ", "yes", "y", "YES", "Yes", "1")
    val no = arrayOf("no", "No", "NO", "2")
    var ans: String? = null

    // Si ya existe un administrador creado, se pregunta al usuario si quiere uno nuevo.
    while (!(ans in si || ans in no)) {
        println(" Ya existe un administrador académico. Si crea uno nuevo, se perderá la información del administrador existente.")
        print(" ¿Desea crear uno nuevo? (1. Sí / 2. No): ")
        ans = readLine()
    }
    if (ans in si) {
        println("\n \u001B[32mSe ha creado exitosamente un nuevo administrador.\u001B[0m\n")
        return true
    } else {
        println("\n \u001B[31mNo se ha creado un nuevo administrador. \u001B[0m\n")
        return false
    }
}

/** 
 * Pide al cliente los datos del carné, materia y calificación para ser añadidos
 * al árbol binario de búsqueda del administrador de información académica.
 *
 * @param[arbol] ArbolInfoAcad que representa el administrador de información académica. 
 */
fun menuAgregar(arbol: ArbolInfoAcad) {
    println(" 2. Agregar información académica:")
    print(" Ingrese el carné del estudiante: ")
    val carne = readLine()!!.trim()
    print(" Ingrese el código de la materia: ")
    val materia = readLine()!!.trim()
    print(" Ingrese la calificación del estudiante: ")
    val c = readLine()
    val calif = c?.trim()?.toIntOrNull()

    try {
        if (calif == null) throw NumberFormatException()
        arbol.agregar(carne, materia, calif)
        println("\n \u001B[32mSe ha agregado exitosamente al estudiante: $carne, materia: $materia, calificación: $calif.\u001B[0m\n")
    } catch (e: ExcepcionMateriaYaCursada) {
        println("\n \u001B[31mError:\u001B[0m No se puede agregar, el estudiante ${carne} ya ha cursado la materia ${materia}. \n")
    } 
}

/** 
 * Pide al cliente los datos del carné y materia para ser eliminados del árbol
 * binario de búsqueda del administrador de información académica.
 *
 * @param[arbol] ArbolInfoAcad que representa el administrador de información académica. 
 */ 
fun menuEliminar(arbol: ArbolInfoAcad) {
    println(" 3. Eliminar información académica:")
    print(" Ingrese el carné del estudiante: ")
    val carne = readLine()!!.trim()
    print(" Ingrese el código de la materia: ")
    val materia = readLine()!!.trim()

    try {
        arbol.eliminar(carne, materia)
        println("\n \u001B[32mSe ha eliminado exitosamente al estudiante: $carne, materia: $materia.\u001B[0m\n")
    } catch (e: ExcepcionMateriaNoCursada) {
        println("\n \u001B[31mError:\u001B[0m no se puede eliminar, el estudiante ${carne} no ha cursado la materia ${materia}.\n")
    }
}

/** 
 * Pide al cliente el carné del estudiante al que se quiere mostrar por consola
 * sus materias y calificaciones.
 *
 * @param[arbol] ArbolInfoAcad que representa el administrador de información académica. 
 */
fun menuListarEst(arbol: ArbolInfoAcad) {
    println(" 4. Listar materias/calificaciones de un estudiante dado:")
    print(" Ingrese carné del estudiante a listar: ")
    val carne = readLine()!!.trim()

    try {
        arbol.mostrarMateriasDeUnEstudiante(carne)
    } catch (e: ExcepcionEstudianteNoHaCursadoMateria) {
        println("\n \u001B[31mError:\u001B[0m No se puede mostrar materias/calificaciones, el estudiante ${carne} no cursa materias. \n")   
    } 
}

/** 
 * Pide al cliente el código de la materia al que se quiere mostrar por consola
 * sus estudiantes y calificaciones.
 *
 * @param[arbol] ArbolInfoAcad que representa el administrador de información académica. 
 */
fun menuListarMat(arbol: ArbolInfoAcad) {
    println(" 5. Listar estudiantes/calificaciones de una materia dada:")
    print(" Ingrese el código de materia a listar: ")
    val materia = readLine()!!.trim()

    try {
        arbol.mostrarEstudiantesDeUnaMateria(materia)
    } catch (e: ExcepcionMateriaNoExistente) {
        println("\n \u001B[31mError:\u001B[0m No se puede mostrar estudiantes/calificaciones, la materia ${materia} no existe en el administrador. \n") 
    } 
}

/** 
 * Pide al cliente el usuario la dirección o camino en el sistema de archivos, 
 * hasta el archivo a cargar (agregar en el arbol de información académica).
 *
 * @param[arbol] ArbolInfoAcad que representa el administrador de información académica.
 * @return true si se cargó con éxito el archivo, en cambio, retorna false.
 */
fun cargarAdminAcad(arbol: ArbolInfoAcad): Boolean {
    println(" 6. Cargar un administrador académico completo desde un archivo dado:")
    print(" Ingrese la dirección del archivo a cargar: ")
    val filename = readLine()!!.trim()
    val fileObject = File(filename)
    val fileExists = fileObject.exists()
    var creado = false

    // Verificamos que el archivo exista
    if (fileExists) {
        var info: Array<String> = arrayOf("", "", "")
        var l = 1

        // Intentamos agregar cada línea del archivo en el árbol
        try {
            fileObject.forEachLine { 
                val datos = (it.split(" ").filter {it != ""}).toTypedArray()
                info = datos
                arbol.agregar(datos[0], datos[1], datos[2].toInt())
                l++
            }
            creado = true
        } catch (e: ExcepcionCalificacionInvalida) {
            println("\n \u001B[31mError:\u001B[0m en la línea $l, la calificación debe ser un entero del intérvalo [1..9]. \n")  
        } catch (e: ExcepcionEstudianteInvalido) {
            println("\n \u001B[31mError:\u001B[0m en la línea $l, el carné debe ser de la forma XX-XXXXX, siendo X un entero [0..9]. \n")
        } catch (e: ExcepcionMateriaInvalida) {
            println("\n \u001B[31mError:\u001B[0m en la línea $l, el código de la materia debe ser de la forma AAXXXX, siendo A un char y X un entero [0..9]. \n") 
        } catch (e: ExcepcionMateriaYaCursada) {
            println("\n \u001B[31mError:\u001B[0m en la línea $l, el estudiante ${info[0]} ya ha cursado la materia ${info[1]}.\n")
        } catch (e: NumberFormatException) {
            println("\n \u001B[31mError:\u001B[0m en la línea $l, la calificación del estudiante ${info[0]} en la materia ${info[1]} debe ser un entero del intérvalo [1..9]. ")
        }    
    } else {
        println(" \u001B[31mError:\u001B[0m no se encontró el archivo.\n")
    }
    return creado
}

/** 
 * Main: Implementación del programa cliente del TAD AIFA. Se le ofrece al usuario 
 * un menú de opciones a efectuar, como crear un administrador, agregar, eliminar,
 * un dato en el administrador, listar por materia, listar por estudiante, cargar un 
 * archivo externo, listar todo el administrador acdémico y cerrar el programa. 
 */
fun main() {
    println("\n \u001B[32m¡BIENVENIDO AL ADMINISTRADOR DE INFORMACIÓN ACADÉMICA! \u001B[0m\n")
    println(" El siguiente programa te permite administrar información académica.")
    println(" Se almacena datos según carné, materia cursada y calificación de cada estudiante.")
    println(" Sólo ingrese el número correspondiente a la opción que quiere ejecutar.\n")
    
    var arbol: ArbolInfoAcad? = null
    var programa = true
    while (programa) {
        try {
            menuOpciones()
            print("\n Ingrese el número de la opción a efectuar (Ej: 1): ")
            val n = readLine()?.trim()?.toIntOrNull()
            println("")

            if (n == null || n < 1 || n > 9) {
                println("\n \u001B[31mError:\u001B[0m Debe introducir un número del 1 al 9 según la opción que desee ejecutar.\n")
            } else if (n == 1) {
                if (arbol == null) {
                    arbol = ArbolInfoAcad()
                    println(" \u001B[32mSe ha creado exitosamente un nuevo administrador.\u001B[0m\n")
                } else {
                    if (menuCrear()) arbol = ArbolInfoAcad()
                }
            } else if (n == 6) {
                val nuevo = ArbolInfoAcad()
                if (cargarAdminAcad(nuevo)) {
                    arbol = nuevo
                    println("\n \u001B[32mSe ha cargado exitosamente el archivo al administrador académico.\u001B[0m\n")
                } else {
                    println(" \u001B[31mError:\u001B[0m No se logró cargar el archivo, no se crea el administrador. \n")
                    if (arbol != null) println(" El administrador preexistente ha sido reestablecido.")
                }
            } else if (n == 9) {
                println(" \u001B[32mSe cierra el programa.\u001B[0m")
                println(" \u001B[32mGracias por usar el Administrador de Información Académica.\u001B[0m")
                println(" \u001B[32m¡Hasta luego! \u001B[0m\n")
                programa = false
            } else if (arbol != null) {
                when (n) {
                    2 -> menuAgregar(arbol)
                    3 -> menuEliminar(arbol)
                    4 -> menuListarEst(arbol)
                    5 -> menuListarMat(arbol)
                    7 -> {
                        println(" 7. Listar la información contenida en el administrador académico")
                        println(arbol.toString())
                    }
                    8 -> {
                        println(" 8. Ejecutar la función sorpresita.") 
                        sorpresita(arbol.iterator())
                    }
                }
            } else {
                println(" \u001B[31mOpción Inválida:\u001B[0m Para ejecutar la opción $n debe haber un administrador creado. \n")
            }
        } catch (e: ExcepcionCalificacionInvalida) {
            println("\n \u001B[31mError:\u001B[0m Calificación inválida, debe ser un entero del intérvalo [1..9].\n ")  
        } catch (e: ExcepcionEstudianteInvalido) {
            println("\n \u001B[31mError:\u001B[0m Carné inválido, debe ser de la forma XX-XXXXX, siendo X un entero [0..9].\n") 
        } catch (e: ExcepcionMateriaInvalida) {
            println("\n \u001B[31mError:\u001B[0m Código de materia inválida, debe ser de la forma AAXXXX siendo A un caracter ['A'..'Z'] en mayúscula y X un entero [0..9].\n") 
        } catch (e: NoSuchElementException) {
            println("\n \u001B[31mError:\u001B[0m no hay mas elementos que iterar del iterador del administrador. \n")
        } catch (e: NumberFormatException) {
            println("\n \u001B[31mError:\u001B[0m la calificación debe ser un entero del intérvalo [1..9]. \n")
        } catch (e: Exception) {
            println("\n \u001B[31mError:\u001B[0m no se pudo ejecutar la opción deseada. \n") 
        } finally {
            println("--------------------------------------------------------------------")
        }
    }
    println(" Programa terminado")
    println("*******************************************") 
}