package blockchainUtils;

import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TransactionToolkit {

	Random rng = new Random();

	/**
	 * Generateur de transaction aléatoire
	 *
	 * @param userCount nombre d'utilisateur de la blockchain
	 * @return une transaction d'un montant aléatoire entre 2 utilisateur aléatoire
	 */
	public String Generator(int userCount) {
		int emetteur = rng.nextInt(userCount) + 1;
		int recepteur;
		do {
			recepteur = rng.nextInt(userCount) + 1;
		} while (recepteur == emetteur);
		long montant = Math.abs(rng.nextLong());
		Transaction tx = new Transaction(emetteur, recepteur, montant);
		return tx.toString();
	}

	/**
	 * Détermine si une chaîne de cacractère est une transaction
	 * 
	 * @param s chaine de caractère à tester
	 * @return true si la chaine de caractère est une transaction
	 */
	public boolean isTransaction(String s) {
		Pattern pattern = Pattern.compile(
				"^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]+)?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)? - Source : user[0-9]+ - Destination : user[0-9]+ - Montant : [0-9]+ - [0-9]+$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	/**
	 * Parser de transaction
	 *
	 * @param transaction Transaction sous la forme d'une chaine de caractère
	 * @return une transaction
	 */

	public Transaction Parser(String transaction) throws UnsupportedOperationException {
		if (!isTransaction(transaction)) {
			throw new UnsupportedOperationException("Malformed transaction");
		}
		// TODO: Genesis transaction parsing
		String[] txstr = transaction.split(" ");
		String date = txstr[0];
		int emetteur = Integer.parseInt(txstr[4].substring(4));
		int recepteur = Integer.parseInt(txstr[8].substring(4));
		long montant = Long.parseLong(txstr[12]);
		int randNb = Integer.parseInt(txstr[14]);
		return new Transaction(date, emetteur, recepteur, montant, randNb);
	}
}
