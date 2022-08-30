KC=	kotlinc
KFLAG=	-cp

all:	\
	ExcepcionCalificacionInvalida.class \
	ExcepcionEstudianteInvalido.class \
	ExcepcionEstudianteNoHaCursadoMateria.class \
	ExcepcionMateriaInvalida.class \
	ExcepcionMateriaNoCursada.class \
	ExcepcionMateriaNoExistente.class \
	ExcepcionMateriaYaCursada.class \
	InfoAcad.class \
	Nodo.class \
	AIFA.class \
	ArbolInfoAcad.class \
	Sorpresita.class \
	MainKt.class

ExcepcionCalificacionInvalida.class: ExcepcionCalificacionInvalida.kt
	$(KC) ExcepcionCalificacionInvalida.kt

ExcepcionEstudianteInvalido.class: ExcepcionEstudianteInvalido.kt
	$(KC) ExcepcionEstudianteInvalido.kt

ExcepcionEstudianteNoHaCursadoMateria.class: ExcepcionEstudianteNoHaCursadoMateria.kt
	$(KC) ExcepcionEstudianteNoHaCursadoMateria.kt

ExcepcionMateriaInvalida.class: ExcepcionMateriaInvalida.kt
	$(KC) ExcepcionMateriaInvalida.kt

ExcepcionMateriaNoCursada.class: ExcepcionMateriaNoCursada.kt
	$(KC) ExcepcionMateriaNoCursada.kt

ExcepcionMateriaNoExistente.class: ExcepcionMateriaNoExistente.kt
	$(KC) ExcepcionMateriaNoExistente.kt

ExcepcionMateriaYaCursada.class: ExcepcionMateriaYaCursada.kt
	$(KC) ExcepcionMateriaYaCursada.kt

InfoAcad.class: InfoAcad.kt
	$(KC) InfoAcad.kt

Nodo.class: Nodo.kt
	$(KC) $(KFLAG) . Nodo.kt

AIFA.class: AIFA.kt
	$(KC) $(KFLAG) . AIFA.kt

ArbolInfoAcad.class: ArbolInfoAcad.kt
	$(KC) $(KFLAG) . ArbolInfoAcad.kt

Sorpresita.class: Sorpresita.kt
	$(KC) $(KFLAG) . Sorpresita.kt

MainKt.class: Main.kt 
	$(KC) $(KFLAG) . Main.kt

clean:
	rm -rf *.class META-INF
