class ExcepcionEstudianteInvalido(e: String): Exception("Error, $e es un carné inválido, debe ser de la forma XX-XXXXX, siendo X un entero [0..9].")