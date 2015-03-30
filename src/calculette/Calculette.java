/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculette;

import java.util.*;
/**
 *
 * @author tinbapakk
 */
public class Calculette {

    
    /**
     * On fixe les opérateurs et leur priorité
     */
    private static final Map <String, Integer> operateurs = new HashMap<String, Integer>() {{
        put("^", 4);
        put("*", 3);
        put("/", 3);
        put("+", 2);
        put("-", 2);
    }};
    
    /**
     * On crée une fonction qui détermine si une priorité est supérieure.
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
     * On crée une fonction qui renvoie le calcul rentré en notation RPN (postfixée)
     */
    public static String calculToPostfix(String calcul)
    {
        // On crée un StringBuilder qui va se remplir avec le résultat
        StringBuilder resultat = new StringBuilder();
        // On crée une liste vide qui va servir de tampon pour arranger le calcul
        ArrayList<String> liste = new ArrayList<String>();
        
        // On fait un foreach sur chaque caractere de la chaine de calcul transformée en tableau par une séparation sur les espaces
        for(String caractere : calcul.split(" ")) {
//            System.out.println("caractere: " + caractere);
            // Si le caractere est un operateur
            if (operateurs.containsKey(caractere)) {
                // tant que la liste n'est pas vide et que la priorité de son dernier element (si c'est bien un opérateur) est supérieure à l'opérateur qu'on vient de trouver, on l'ajoute au résultat
                while (!liste.isEmpty() && prioriteSuperieure(caractere, liste.get(liste.size()-1))) {
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
        }
        
        // Après le foreach, tant que la liste est pleine, on la vide dans le résultat.
        while (!liste.isEmpty()) {
            resultat.append(liste.get(liste.size()-1)).append(" ");
            liste.remove(liste.size()-1);
        }
        return resultat.toString();
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        System.out.println(calculToPostfix("( 5 + 7 ) * 2"));
    }
    
}
