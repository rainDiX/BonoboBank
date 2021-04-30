package bcb;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.*;

public class TransactionToolkit {

	Random rng = new Random();

	/**
	 * logger pour gérer les messages de deboguage
	 */
	private static Logger logr = Logger.getLogger(CentralBank.class.getName());

	/**
	 * Generateur de transaction aléatoire
	 *
	 * @param userList liste d'utilisateur de la blockchain
	 * @return une transaction d'un montant aléatoire entre 2 utilisateur aléatoire
	 */
	public Transaction Generate(ArrayList<User> userList) {
		int emIndex = rng.nextInt(userList.size());
		int recIndex = rng.nextInt(userList.size());
		while (recIndex == emIndex)
			recIndex = rng.nextInt(userList.size());
		long montant = Math.abs(rng.nextLong());
		Transaction tx = new Transaction(userList.get(emIndex).getName(), userList.get(recIndex).getName(), montant);
		return tx;
	}

	/**
	 * Détermine si une chaîne de cacractère est une transaction
	 * 
	 * @param s chaine de caractère à tester
	 * @return true si la chaine de caractère est une transaction
	 */
	public boolean isTransaction(String s) {
		Pattern pattern = Pattern.compile(
				"^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]+)?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)? - Source : [a-zA-Z]+[0-9]* - Destination : [a-zA-Z]+[0-9]* - Montant : [0-9]+ - [0-9]+$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches() || this.isGenesis(s);
	}

	public boolean isGenesis(String s) {
		return s == "Genesis";
	}

	/**
	 * Parser de transaction
	 *
	 * @param transaction Transaction sous la forme d'une chaine de caractère
	 * @return une transaction
	 */

	public Transaction Parse(String transaction) throws UnsupportedOperationException {
		if (!isTransaction(transaction)) {
			logr.severe("'" + transaction + "' n'est pas une transaction valide");
			throw new UnsupportedOperationException("Malformed transaction");
		}
		if (isGenesis(transaction)) {
			throw new UnsupportedOperationException("Cannot create a transaction from the Geneis transaction");
		}
		String[] txstr = transaction.split(" ");
		String date = txstr[0];
		String emetteur = txstr[4];
		String recepteur = txstr[8];
		long montant = Long.parseLong(txstr[12]);
		int randNb = Integer.parseInt(txstr[14]);
		return new Transaction(date, emetteur, recepteur, montant, randNb);
	}
}
