package it.unibg.progetto.api.domain.rules;


/**
 * columnIndex = indice della colonna: 0 = id 1 = nome 2 = cognome 3 = mail 4 =
 * numero_di_telefono 5 = ruolo 6 = anno_inizio 7 = richiami
 */
public enum StringValues {

	ID(0), NOME(1), COGNOME(2), MAIL(3), NUMERO_TELEFONO(4), RUOLO(5), ANNO_INIZIO(6), RICHIAMI(7);

	private final int index;

	StringValues(int index) {
		this.index = index;
	}

	/** Ritorna l’indice numerico associato all’enum */
	public int getIndex() {
		return index;
	}

	/** Ritorna l’enum corrispondente all’indice */
	public static StringValues fromIndex(int index) {
		for (StringValues v : values()) {
			if (v.index == index)
				return v;
		}
		throw new IllegalArgumentException("Indice non valido: " + index);
	}
}
