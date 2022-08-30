import kotlin.text.isUpperCase
import kotlin.text.isDigit
import java.lang.StringBuilder

/**
 * Implementación de la interfaz AIFA, basado en un árbol binario
 * de búsqueda. Cada nodo del árbol es de tipo InfoAcad.
 * Contiene las operaciones de agregar, eliminar mostrar materias de un 
 * estudiante mostrar estudiantes de una materia, la representación en 
 * String del árbol y un iterador del árbol de información académica.
 */
class ArbolInfoAcad : AIFA {
    private var raizEMC: Nodo? = null
    
    /********************** INTERFAZ AIFA **********************/

    /** 
     * Agrega una nueva materia dado el carné de un estudiante y su calificación.
     *
     * @param[e] Arreglo de Strings que representa el carnet del estudiante.
     * @param[m] Arreglo de Strings que representa una materia.
     * @param[c] Entero que representa la calificación de una materia.
     */
    override fun agregar(e: String, m: String, c: Int) {
        if (!carneValido(e)) throw ExcepcionEstudianteInvalido(e)
        if (!codigoValido(m)) throw ExcepcionMateriaInvalida(m)
        if (c < 1 || c > 9) throw ExcepcionCalificacionInvalida(c)
        if (materiasCursadas(e, m)) throw ExcepcionMateriaYaCursada(e, m)
        //if (!infoValida(raizEMC) || !(esArbolDeBusqEstudMat(raizEMC))) {
        //    throw Exception("Error, el árbol no tiene información válida o no cumple las propiedades de un ABB")
        //}

        val emc = InfoAcad(e, m, c)         // Crear un nuevo InfoAcad
        val z = Nodo(emc)                   // Crear nuevo nodo 

        // Agregar nuevo nodo al áŕbol
        var y: Nodo? = null
        var x = raizEMC
        while (x != null) {
            y = x
            x = if (z.valor < x.valor) x.hijoIzq else x.hijoDer
        }

        // Actualizar el hijo de y
        z.padre = y
        if (y == null) {
            raizEMC = z
        } else if (z.valor < y.valor) {
            y.hijoIzq = z
        } else {
            y.hijoDer = z
        } 
    }

    /** 
     * Elimina la información de una materia cursada por un estudiante.
     *
     * @param[e] Arreglo de Strings que representa el carnet del estudiante
     * @param[m] Arreglo de Strings que representa una materia
     */
    override fun eliminar(e: String, m: String) {
        if (!carneValido(e)) throw ExcepcionEstudianteInvalido(e)
        if (!codigoValido(m)) throw ExcepcionMateriaInvalida(m)
        if (!(materiasCursadas(e, m))) throw ExcepcionMateriaNoCursada(e, m)
        //if (!infoValida(raizEMC) || !(esArbolDeBusqEstudMat(raizEMC))) {
        //    throw Exception("Error, el árbol no tiene información válida o no cumple las propiedades de un ABB")
        //}

        val info = InfoAcad(e, m, 1)        // Creamos un nodo para buscar
        val z = buscar(raizEMC, info)       // Buscamos el nodo a eliminar

        // Si el nodo a eliminar no tiene hijo izq o der, sólo se sutituye por el otro hijo.
        // En cambio, se debe buscar el mínimo del hijo derecho para suplantar al eliminado.
        if (z?.hijoIzq == null) {
            transplant(z, z?.hijoDer)
        } else if (z.hijoDer == null) {
            transplant(z, z.hijoIzq)
        } else {
            val y = minimo(z.hijoDer)
            if (y?.padre != z) {
                transplant(y, y?.hijoDer)
                y?.hijoDer = z.hijoDer
                y?.hijoDer?.padre = y
            }
            transplant(z, y)
            y?.hijoIzq = z.hijoIzq
            y?.hijoIzq?.padre = y
        }
    }

    /** 
     * Imprime por la salida estándar los materias que ha cursado 
     * un estudiante con sus respectivas calificaciones, ordenadas 
     * por materias.
     *
     * @param[e] Arreglo de Strings que representa el carnet del estudiante
     */
    override fun mostrarMateriasDeUnEstudiante(e: String) {
        if (!carneValido(e)) throw ExcepcionEstudianteInvalido(e)
        if (!existeEstudiante(e)) throw ExcepcionEstudianteNoHaCursadoMateria(e)

        println("\n Estudiante: $e \n Materia | Calificación \n ----------------------")
        mostrarMatEst(raizEMC, e, true)
        println("")
    }

    /** 
     * Muestra por la salida estándar a todos los estudiantes que han cursado 
     * una materia, junto con su calificación, ordenados por el carné del estudiante. 
     *
     * @param[m] Arreglo de Strings que representa una materia
     */
    override fun mostrarEstudiantesDeUnaMateria(m: String) {
        if (!codigoValido(m)) throw ExcepcionMateriaInvalida(m)
        if (!existeMateria(m)) throw ExcepcionMateriaNoExistente(m)

        println("\n Materia: $m \n Estudiante | Calificación \n -------------------------")
        mostrarMatEst(raizEMC, m, false)
        println("")
    }

    /** 
     * Construye el objeto Iterador del árbol de información académico. 
     *
     * @param[l] Árbol binario de búsqueda.
     * @return Iterador del árbol donde cada elemento es del tipo InfoAcad.
     */
    inner class ArbolInfoAcadIterator(l: ArbolInfoAcad) : Iterator<InfoAcad> {
        val arbol = l
        var actual: Nodo? = arbol.minimo(raizEMC)

         /** Verifica si hay elementos que iterar en el árbol */
        override fun hasNext() : Boolean = actual != null

        /** Retorna el elemento de tipo InfoAcad del siguiente elemento del árbol */
        override fun next(): InfoAcad {
            if (actual == null) throw NoSuchElementException("Error, no hay mas elementos que iterar.")
			val valor = actual!!.valor              // Se guarda el valor a retornar
            actual = arbol.sucesor(actual)          // Actualizar el nodo actual
            return valor
        }
    }

    /** Iterador tipo InfoAcad de la clase ArbolInfoAcad */
    override operator fun iterator() : Iterator<InfoAcad> = ArbolInfoAcadIterator(this)

    /** 
     * Retorna una representación de todas las tripletas estudiantes, materia
     * y calificaciones de la instancia AIFA.
     *
     * @return String que representa árbol de información académico.
     */
    override fun toString(): String {
        if (raizEMC == null) return "El administrador académico se encuentra vacío.\n"
        val s = StringBuilder(" ESTUDIANTE | MATERIA | CALIFICACIÓN \n ---------------------------------")
        for (x in this) s.append("\n  ${x.carnet}  | ${x.materia}  |  ${x.calificacion}")
        s.append("\n")
        return s.toString()
    }
    
    /********************** FUNCIONES AUXILIARES **********************/

    /** 
     * Verifica si el carné de un estudiante es válido. Un carnet válido
     * es de la forma XX-XXXXX, donde cada X es un entero entre [0..9].
     * 
     * @param[s] String que representa el carné del estudiante.
     * @return true si el carné es válido, en cambio, retorna False.
     */
    private fun carneValido(s: String): Boolean {
        var valido = (s.length == 8) && (s[2] == '-')
        for (i in 0 until 2) valido = valido && s[i].isDigit()
        for (i in 3 until 8) valido = valido && s[i].isDigit()
        return valido
    }

    /** 
     * Verifica si el código de una materia es válida. Una materia válida
     * es de la forma AAXXXXX, donde cada X es un entero entre [1..9] y
     * cada A es un caracter alfabético en mayúscula ['A'..'Z'].
     * 
     * @param[s] String que representa el código de la materia.
     * @return true si la materia es válida, en cambio, retorna False.
     */
    private fun codigoValido(s: String): Boolean {
        var valido = (s.length == 6) && s[0].isUpperCase() && s[1].isUpperCase()
        for (i in 2 until 6) valido = valido && s[i].isDigit()
        return valido
    }

    /** 
     * Verifica si un estudiante cursa una materia dada. 
     *
     * @param[e] String que representa el carné del estudiante. 
     * @param[m] String que representa el código de la materia.
     * @return true si el estudiante cursa la materia, en cambio, retorna False.
     */
    private fun materiasCursadas(e: String, m: String): Boolean {
        val nuevo = InfoAcad(e, m, 1)
        return buscar(raizEMC, nuevo) != null
    }

    /** 
     * Verifica si un estudiante se encuentra en el árbol de información académica.
     *
     * @param[e] String que representa el carné del estudiante. 
     * @return true si el estudiante existe, en cambio, retorna False.
     */
    private fun existeEstudiante(e: String): Boolean {
        var x = raizEMC
        while (x != null) {
            if (x.valor.carnet == e) return true
            x = if (e < x.valor.carnet) x.hijoIzq else x.hijoDer
        }
        return false
    }

    /** 
     * Recorre de manera recursiva el árbol para verificar si una materia existe.
     *
     * @param[x] Nodo al que se recorre en el árbol. 
     * @param[m] String que representa el código de la materia.
     * @param[existe] Boolean que indica si la materia dada coincide con la materia del nodo.
     * @return true si la materia existe, en cambio, retorna False. 
     */
    private fun existeMatRecur(x: Nodo?, m: String, existe: Boolean): Boolean {
        var e = existe
        if (x != null && !e) {
            e = existeMatRecur(x.hijoIzq, m, e || (x.valor.materia == m))
            e = existeMatRecur(x.hijoDer, m, e || (x.valor.materia == m))
        }
        return e
    }

    /** 
     * Verifica si una materia se encuentra en el árbol de información académica.
     *
     * @param[m] String que representa el código de la materia.
     * @return true si la materia existe, en cambio, retorna False. 
     */
    private fun existeMateria(m: String): Boolean = existeMatRecur(raizEMC, m, false)

    /** 
     * Buscar desde un nodo [x] si hay un nodo es que su atributo InfoAcad es igual a [k]. 
     *
     * @param[x] Nodo del árbol donde se comienza a recorrer.
     * @param[k] InfoAcad al que se quiere buscar en los nodos del árbol.
     * @return null si no encuentra el árbol, en cambio, el Nodo con la misma clave [k].
     */
    private fun buscar(x: Nodo?, k: InfoAcad): Nodo? {
        var nodo = x
        while (nodo != null && k != nodo.valor) {
            nodo = if (k < nodo.valor) nodo.hijoIzq else nodo.hijoDer
        }
        return nodo
    }

    /** 
     * Retorna el nodo mínimo del árbol desde un nodo [a] 
     *
     * @param[a] Nodo del árbol donde se comienza a recorrer.
     * @return null si no tiene mínimo, en cambio, el Nodo mínimo del árbol.
     */
    private fun minimo(a: Nodo?): Nodo? {
        var x: Nodo? = a
        while (x?.hijoIzq != null) x = x.hijoIzq
        return x
    }

    /** 
     * Retorna el nodo sucesor del nodo [a] 
     *
     * @param[a] Nodo del árbol al que se quiere buscar el sucesor.
     * @return null si no existe sucesor, en cambio, el Nodo sucesor de [a].
     */
    private fun sucesor(a: Nodo?): Nodo? {
        var x: Nodo? = a
        if (x?.hijoDer != null) return minimo(x.hijoDer)
        var y = x?.padre
        while (y != null && x == y.hijoDer) {
            x = y
            y = y.padre
        }
        return y
    }

    /** 
     * Reemplaza un subárbol con nodo raíz [u] con otro subárbol con nodo raíz [v]
     *
     * @param[u] Nodo a ser reemplazado
     * @param[v] Nodo que será el reemplazo
     */
    private fun transplant(u: Nodo?, v: Nodo?) {
        if (u?.padre == null) {
            raizEMC = v
        } else if (u == u.padre?.hijoIzq) {
            u.padre?.hijoIzq = v
        } else {
            u.padre?.hijoDer = v
        }
        if (v != null) v.padre = u?.padre
    }

    /**
     * Recorre el árbol binario de búsqueda e imprime según una materia
     * o un estudiante dado.
     *  
     * @param[x] Nodo al que se recorre.
     * @param[e] Arreglo de Strings que representa el carnet del estudiante o la materia.
     * @param[isCarnet] Booleano que indica si se muestra dado un carnet o una materia.
     */
    private fun mostrarMatEst(x: Nodo?, e: String, isCarnet: Boolean) {
        if (x != null) {
            mostrarMatEst(x.hijoIzq, e, isCarnet)
            if (isCarnet && x.valor.carnet == e) println("  ${x.valor.materia} |  ${x.valor.calificacion}")
            if (!(isCarnet) && x.valor.materia == e) println("  ${x.valor.carnet}  |  ${x.valor.calificacion}")
            mostrarMatEst(x.hijoDer, e, isCarnet)
        }
    }

    /********************** FUNCIONES NO OBLIGATORIAS **********************/

    /** 
     * Retorna el carné mínimo del árbol, desde un nodo [nodo].
     *
     * @param[nodo] Nodo del árbol donde se quiere empezar a buscar el mínimo carné.
     * @return String que representa el carnet mínimo del árbol.
     */
    private fun minCarne(nodo: Nodo?): String? {
        if (nodo == null) return null
        val min = minimo(nodo)
        return min?.valor?.carnet
    }

    /** 
     * Retorna el carné máximo del árbol, desde un nodo [nodo].
     *
     * @param[nodo] Nodo del árbol donde se quiere empezar a buscar el máximo carné.
     * @return String que representa el carnet máximo del árbol.
     */
    private fun maxCarne(nodo: Nodo?): String? {
        if (nodo == null) return null
        var x: Nodo? = nodo
        while (x?.hijoDer != null) x = x.hijoDer
        return x?.valor?.carnet
    }

    /** 
     * Retorna la mínima o máxima materia del árbol de búsqueda.
     *
     * @param[nodo] Nodo del árbol donde se quiere empezar a buscar el máximo carné.
     * @param[mat] String que guarda la materia mínima o máxima del árbol
     * @param[isMin] Boolean que indica si se busca el mínimo o máximo de una materia.
     * @return String que representa la materia mínima o máxima del árbol.
     */
    private fun minMaxMateria(x: Nodo?, mat: String, isMin: Boolean): String {
        var m = mat
        if (x != null)  {
            // Recorrer el lado izquierdo del árbol
            if (((isMin) && (m > x.valor.materia)) || (!(isMin) && (m < x.valor.materia))) {
                m = minMaxMateria(x.hijoIzq, x.valor.materia, isMin)
            } else {
                m = minMaxMateria(x.hijoIzq, m, isMin)
            }
            // Recorrer el lado derecho del árbol
            if (((isMin) && (m > x.valor.materia)) || (!(isMin) && (m < x.valor.materia))) {
                m = minMaxMateria(x.hijoDer, x.valor.materia, isMin)
            } else {
                m = minMaxMateria(x.hijoDer, m, isMin)
            }
        }
        return m
    }

    /** 
     * Retorna el menor valor del atributo materia en el árbol de búsqueda.
     *
     * @param[nodo] Nodo del árbol donde se quiere empezar a buscar la mínima materia.
     * @return String que representa la materia mínima del árbol.
     */
    private fun minMateria(nodo: Nodo?): String? {
        if (nodo == null) return null
        return minMaxMateria(nodo, nodo.valor.materia, true)
    }

    /** 
     * Retorna el mayor valor del atributo materia en el árbol de búsqueda.
     *
     * @param[nodo] Nodo del árbol donde se quiere empezar a buscar la máxima materia.
     * @return String que representa la materia máxima del árbol.
     */
    private fun maxMateria(nodo: Nodo?): String? {
        if (nodo == null) return null
        return minMaxMateria(nodo, nodo.valor.materia, false)
    }

    /** 
     * Determina si todos los nodos de un ArbolInfoAcad poseen información académica válida
     * de manera recursiva. 
     *
     * @param[nodo] Nodo del árbol al que se quiere verificar.
     * @return true si los nodos poseen información válida, en cambio, false.
     */
    private fun infoValida(nodo: Nodo?): Boolean {
        if (nodo == null) return true
        val carne = carneValido(nodo.valor.carnet) 
        val materia = codigoValido(nodo.valor.materia)
        val calif = (nodo.valor.calificacion > 0) && (nodo.valor.calificacion < 10)
        return carne && materia && calif && infoValida(nodo.hijoIzq) && infoValida(nodo.hijoDer)
    }

    /**
     * Determina si una estructura de tipo ArbolInfoAcad, es un árbol binario 
     * de búsqueda en donde la información académica está ordenada por medio de 
     * la relación estudiante/materia.
     * 
     * @param[nodo] Nodo del árbol al que se quiere verificar.
     * @return true si el árbol está ordenada según estudiante/materia, en cambio, false.
     */
    private fun esArbolDeBusqEstudMat(nodo: Nodo?): Boolean {
        if (nodo == null ) return true

        val minCarnet = minCarne(nodo)
        val maxCarnet = maxCarne(nodo)
        val minMat = minMateria(nodo)
        val maxMat = maxMateria(nodo)

        if (minCarnet == null || maxCarnet == null || minMat == null || maxMat == null) return true
        
        val carnet = nodo.valor.carnet
        val carneMin = ((carnet > minCarnet) || (carnet == minCarnet && nodo.valor.materia >= minMat)) 
        val carneMax = ((carnet < maxCarnet) || (carnet == maxCarnet && nodo.valor.materia <= maxMat))

        return carneMin && esArbolDeBusqEstudMat(nodo.hijoDer) && esArbolDeBusqEstudMat(nodo.hijoIzq) && carneMax
    }

    /**
     * Se extrae del ArbolInfoAcad toda la información académica que posee.
     * 
     * @param[arbol] Árbol de información académica de tipo ArbolInfoAcad.
     * @return Arreglo de todas las tripletas (e, m, c) de los nodos del ArbolInfoAcad.
     */
    private fun extraccionInfo(arbol: ArbolInfoAcad): Array<Triple<String, String, Int>> {
        var conjunto: Array<Triple<String, String, Int>> = arrayOf()
        var actual: Nodo? = arbol.minimo(arbol.raizEMC)

        while (actual != null) {
            conjunto += Triple(actual.valor.carnet, actual.valor.materia, actual.valor.calificacion)
            actual = arbol.sucesor(actual)
        }

        return conjunto
    }
}