package calculette;

import java.util.*;
/**
 *
 * @author Romain
 */
public class CalculetteV3 {

    
    /**
     * On fixe les opérateurs et leur priorité
     */
    public static final Map <String, Integer> operateurs = new HashMap<String, Integer>() {{
        put("^", 4);
        put("*", 3);
        put("/", 3);
        put("+", 2);
        put("-", 2);
    }};
    
    /**
     * On vérifie si la chaine est bien un operateur, et si sa priorité est supérieure à celle de l'opérateur comparé
     * @param operateur L'operateur qu'on veut comparer à une chaine
     * @param chaine La chaine à comparer à l'opérateur.
     * @return boolean 
     */
    private static boolean prioriteSuperieure(String operateur, String chaine)
    {
        return (operateurs.containsKey(chaine) && operateurs.get(chaine) >= operateurs.get(operateur));
    }
    
    /**
     * On crée une fonction qui renvoie le calcul rentré en notation RPN (postfixée) : algorithme Shunting yard de Dijkstra
     * @param calcul Le calcul initial, formaté en string et en mode infixée (mode classique de représentation de calcul, tel qu'il sera tapé dans la calculette, ex: (2+3)*4/9)
     * @return resultat Le calcul transformée en notation postfixée: sans parenthèse, le calcul se lit linéairement de gauche à droite, 2 opérandes pour un opérateur. Tous les opérateurs s'associent avec l'opérande de gauche, à l'exception de la puissance (ex du dessus: 2 3 + 4 * 9 /)
     */
    public static String calculToPostfix(String calcul)
    {
        // On crée un StringBuilder qui va se remplir avec le résultat
        StringBuilder resultat = new StringBuilder();
        // On crée une liste vide qui va servir de tampon pour arranger le calcul
        ArrayList<String> liste = new ArrayList<>();
        
        // On fait un foreach sur chaque caractere de la chaine de calcul transformée en tableau par une séparation sur les espaces
        for(String caractere : calcul.split(" ")) {
//            System.out.println("caractere: " + caractere);
            // Si le caractere est un operateur
            if (operateurs.containsKey(caractere)) {
                // tant que la liste n'est pas vide et que la priorité de son dernier element (si c'est bien un opérateur) est supérieure à l'opérateur qu'on vient de trouver, on l'ajoute au résultat.
                // On exclut les puissances de la comparaison car elles auraient une priorité identique et leur association se fait d'abord avec l'opérande de droite (contrairement aux autres opérateurs qui s'associent avec l'opérande de gauche.
                // Ex: 5^2^3 = 5^(2^3) != (RPN) 5 ^ 2 ^ 3 = (RPN) 5 2 3 ^ ^)
                while (!liste.isEmpty() &&
                        (prioriteSuperieure(caractere, liste.get(liste.size()-1)) && !caractere.equals("^"))
                         ) {

                    resultat.append(liste.get(liste.size()-1)).append(" ");
                    liste.remove(liste.size()-1);
                }
                // Sinon on ajoute l'opérateur à la liste
                liste.add(caractere);
            
            // Si c'est une parenthese ouvrante
            } else if (caractere.equals("(")) {
                // On l'ajoute à la liste
                liste.add(caractere);
                
            // Si c'est une parenthese fermante
            } else if (caractere.equals(")")) {
                // tant que le dernier element de la liste n'est pas une parenthese ouvrante, on vite la liste dans le resultat
                while (!liste.get(liste.size()-1).equals("(")) {
                    resultat.append(liste.get(liste.size()-1)).append(" ");
                    liste.remove(liste.size()-1);
                }
                // Puis on dégage le dernier élement restant (càd la parenthese ouvrante)
                liste.remove(liste.size()-1);
                
            // Si c'est un chiffre
            } else {
                // On l'ajoute direct au résultat
                resultat.append(caractere).append(" ");
            }
//            System.out.println("liste: " + liste);
//            System.out.println("resultat: " + resultat);
//            System.out.println("-----------");
        }
        
        // Après le foreach, tant que la liste est pleine, on la vide dans le résultat.
        while (!liste.isEmpty()) {
            resultat.append(liste.get(liste.size()-1)).append(" ");
            liste.remove(liste.size()-1);
        }
        return resultat.toString();
    }
    
    public static String postfixToResultat(String postfix)
    {
        String resultat;
        // On crée une liste vide qui va servir de tampon pour faire le calcul
        ArrayList<Double> liste = new ArrayList<>();
        
        // On fait un foreach sur chaque caractere de la chaine de calcul transformée en tableau par une séparation sur les espaces
        for(String nombre : postfix.split(" ")) {
//            System.out.println("caractere: " + nombre);
            
            // D'abord on check si le nombre est bien un nombre (et non un opérateur)
            Double nombreDecimal = null;
            try {
                nombreDecimal = Double.parseDouble(nombre);
            } catch (NumberFormatException e) {} 
            if (nombreDecimal != null) {
                // Si c'est bien le cas, on le stocke dans la liste en tant que double
                liste.add(Double.parseDouble(nombre));
//                System.out.println(liste);
            } else if ("^".equals(nombre)) {
                Double operande2 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                Double operande1 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                liste.add(Math.pow(operande1, operande2));
            } else if ("*".equals(nombre)) {
                Double operande2 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                Double operande1 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                liste.add(operande1 * operande2);
            } else if ("/".equals(nombre)) {
                Double operande2 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                Double operande1 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                liste.add(operande1 / operande2);
            } else if ("+".equals(nombre)) {
                Double operande2 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                Double operande1 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                liste.add(operande1 + operande2);
            } else if ("-".equals(nombre)) {
                Double operande2 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                Double operande1 = liste.get(liste.size()-1);
                liste.remove(liste.size()-1);
                liste.add(operande1 - operande2);
            } else {
                return resultat = "Erreur";
            }
        }
//        System.out.println(liste);
        return resultat = String.valueOf(liste.get(liste.size()-1));
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        System.out.println(calculToPostfix("( - 2 + 3 ) * ( ( 4 / 9 ) ^ ( - 2 * 5 ) ^ 3 )"));
        System.out.println(postfixToResultat(calculToPostfix("( - 2 + 3 ) * ( ( 4 / 9 ) ^ ( - 2 * 5 ) ^ 3 )")));
    }
    
}
