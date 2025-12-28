package com.hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Llistar reserves per tipus");
        System.out.println("5. Obtindre una reserva");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
       //TODO:
        switch (opcio) {
        case 1:
            reservarHabitacio();
            break;
        case 2:
            alliberarHabitacio();
            break;
        case 3:
            consultarDisponibilitat();
            break;
        case 4:
            obtindreReservaPerTipus();
            break;
        case 5:
            obtindreReserva();
            break;
        case 6:
            // salir: el main ya controla el bucle
            break;
        default:
            System.out.println("Opció no vàlida. Torna a intentar-ho.");
            break;
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        //TODO:
        // 1) Seleccionar tipus disponible
        String tipus = seleccionarTipusHabitacioDisponible();
        if (tipus == null) {
            System.out.println("No s'ha pogut completar la reserva: no hi ha habitacions disponibles d'aquest tipus.");
            return;
        }

        // 2) Seleccionar serveis
        ArrayList<String> serveisSeleccionats = seleccionarServeis();

        // 3) Calcular preu
        float total = calcularPreuTotal(tipus, serveisSeleccionats);

        // 4) Generar codi únic
        int codi = generarCodiReserva();

        // 5) Guardar reserva en el HashMap (estructura ArrayList<String>)
        ArrayList<String> dades = new ArrayList<String>();
        dades.add(tipus);
        // arrodonim a 2 decimals
        float rounded = Math.round(total * 100f) / 100f;
        dades.add(String.valueOf(rounded));
        // afegim serveis (si n'hi ha)
        if (serveisSeleccionats != null) {
            dades.addAll(serveisSeleccionats);
        }

        reserves.put(codi, dades);

        // 6) Actualitzar disponibilitat
        int disponibles = disponibilitatHabitacions.get(tipus);
        disponibilitatHabitacions.put(tipus, disponibles - 1);

        // 7) Confirmació
        System.out.println("\nReserva creada amb èxit!");
        System.out.println("Codi de reserva: " + codi);
        System.out.println("Total a pagar: " + String.valueOf(rounded) + "€");
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        //TODO:
        while (true) {
            System.out.println("\nSeleccione tipus d'habitació:");
            System.out.println("1 - " + TIPUS_ESTANDARD);
            System.out.println("2 - " + TIPUS_SUITE);
            System.out.println("3 - " + TIPUS_DELUXE);
            int eleccio = llegirEnter("Opció: ");
            switch (eleccio) {
                case 1:
                    return TIPUS_ESTANDARD;
                case 2:
                    return TIPUS_SUITE;
                case 3:
                    return TIPUS_DELUXE;
                default:
                    System.out.println("Opció no vàlida. Intenta-ho de nou.");
            }
        }
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d'habitació disponibles:");
        //TODO:
        mostrarInfoTipus(TIPUS_ESTANDARD);
        mostrarInfoTipus(TIPUS_SUITE);
        mostrarInfoTipus(TIPUS_DELUXE);

        String tipus = seleccionarTipusHabitacio();
        int disponibles = disponibilitatHabitacions.get(tipus);
        if (disponibles > 0) {
            return tipus;
        } else {
            System.out.println("Ho sentim, no queden habitacions del tipus seleccionat.");
            return null;
        }
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        //TODO:
        ArrayList<String> seleccionats = new ArrayList<String>();
        String[] serveis = {SERVEI_ESMORZAR, SERVEI_GIMNAS, SERVEI_SPA, SERVEI_PISCINA};
        System.out.println("\nServeis addicionals (0-4):");
        System.out.println("0 - Finalitzar");
        for (int i = 0; i < serveis.length; i++) {
            System.out.println((i+1) + " - " + serveis[i] + " (" + preusServeis.get(serveis[i]) + "€)");
        }

        while (true) {
            System.out.print("Vol afegir un servei? (s/n): ");
            String resp = sc.next();
            if (resp.length() == 0) continue;
            char r = Character.toLowerCase(resp.charAt(0));
            if (r == 'n') {
                break;
            } else if (r == 's') {
                int sel = llegirEnter("Seleccione servei (1-4): ");
                if (sel < 1 || sel > 4) {
                    System.out.println("Servei no vàlid.");
                    continue;
                }
                String s = serveis[sel - 1];
                if (seleccionats.contains(s)) {
                    System.out.println("Servei ja seleccionat.");
                } else {
                    seleccionats.add(s);
                    System.out.println("Servei afegit: " + s);
                    if (seleccionats.size() == 4) {
                        System.out.println("Has arribat al màxim de serveis.");
                        break;
                    }
                }
            } else {
                System.out.println("Resposta no vàlida, introdueix 's' o 'n'.");
            }
        }
        return seleccionats;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        //TODO:
        float preuHab = preusHabitacions.get(tipusHabitacio);
        float totalServeis = 0f;
        if (serveisSeleccionats != null) {
            for (String s : serveisSeleccionats) {
                Float p = preusServeis.get(s);
                if (p != null) totalServeis += p;
            }
        }
        float subtotal = preuHab + totalServeis;
        float total = subtotal * (1 + IVA);
        // arrodonir a 2 decimals
        total = Math.round(total * 100f) / 100f;
        return total;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        //TODO:
        int codi;
        do {
            codi = 100 + random.nextInt(900); // 100..999
        } while (reserves.containsKey(codi));
        return codi;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
         // TODO: Demanar codi, tornar habitació i eliminar reserva
        int codi = llegirEnter("Introduïx el codi de reserva: ");
        if (!reserves.containsKey(codi)) {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
            return;
        }
        ArrayList<String> dades = reserves.get(codi);
        String tipus = dades.get(0);
        // actualitzar disponibilitat
        int disponibles = disponibilitatHabitacions.get(tipus);
        disponibilitatHabitacions.put(tipus, disponibles + 1);
        // eliminar reserva
        reserves.remove(codi);
        System.out.println("Reserva trobada!");
        System.out.println("Habitació alliberada correctament.");
        System.out.println("Disponibilitat actualitzada.");
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        // TODO: Mostrar lliures i ocupades
        System.out.println("\n===== DISPONIBILITAT D'HABITACIONS =====");
        System.out.println("Tipus\tLliures\tOcupades");
        mostrarDisponibilitatTipus(TIPUS_ESTANDARD);
        mostrarDisponibilitatTipus(TIPUS_SUITE);
        mostrarDisponibilitatTipus(TIPUS_DELUXE);
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
         // TODO: Implementar recursivitat
        if (codis == null || codis.length == 0) {
            return;
        }
        int primer = codis[0];
        if (reserves.containsKey(primer)) {
            ArrayList<String> dades = reserves.get(primer);
            if (dades.get(0).equals(tipus)) {
                mostrarDadesReserva(primer);
            }
        }
        // crear nou array sense la posició 0
        int[] newCodis = new int[codis.length - 1];
        if (newCodis.length > 0) {
            System.arraycopy(codis, 1, newCodis, 0, newCodis.length);
        }
        llistarReservesPerTipus(newCodis, tipus);
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        // TODO: Mostrar dades d'una reserva concreta
        int codi = llegirEnter("Introduïx el codi de reserva: ");
        if (!reserves.containsKey(codi)) {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
            return;
        }
        mostrarDadesReserva(codi);
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        // TODO: Llistar reserves per tipus
        String tipus = seleccionarTipusHabitacio();
        System.out.println("\nReserves del tipus \"" + tipus + "\":\n");
        // obtenir tots els codis
        if (reserves.isEmpty()) {
            System.out.println("No hi ha reserves registrades.");
            return;
        }
        int[] codis = new int[reserves.size()];
        int i = 0;
        for (Integer key : reserves.keySet()) {
            codis[i++] = key;
        }
        llistarReservesPerTipus(codis, tipus);
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
       // TODO: Imprimir tota la informació d'una reserva
        if (!reserves.containsKey(codi)) {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
            return;
        }
        ArrayList<String> dades = reserves.get(codi);
        System.out.println("Codi: " + codi);
        System.out.println("Tipus d'habitació: " + dades.get(0));
        System.out.println("Cost total: " + dades.get(1) + "€");
        if (dades.size() > 2) {
            System.out.println("Serveis addicionals:");
            for (int j = 2; j < dades.size(); j++) {
                System.out.println("- " + dades.get(j));
            }
        } else {
            System.out.println("Serveis addicionals: (cap)");
        }
        System.out.println(); // línia buida final per llegibilitat
        }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
                System.out.print(missatge);
                valor = sc.nextInt();
                correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
