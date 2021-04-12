package blockchainUtils;

import java.time.*;
import java.util.Random;

import miscUtils.HashUtil;

public class Transaction {

	/**
	 * Borne sup du nombre aléatoire associé à la transaction
	 */
	static final int MAX_VALUE = 10000;

	/**
	 * Nombre aléatoire associé à la transaction
	 */
	private int randNb;

	/**
	 * Date de la transaction
	 */
	private String date;

	/**
	 * Emetteur de la transaction
	 */
	private int emetteur;

	/**
	 * Récepteur de la transaction
	 */
	private int recepteur;

	/**
	 * Montant de la transaction en SatoBnB
	 */
	private long montant;

	/**
	 * Constructeur de transaction
	 * 
	 * @param emetteur Émetteur de la transaction
	 * @param recepteur Récepteur de la transaction
	 * @param montant Montant de la transaction en SatoBnB
	 */
	public Transaction(int emetteur, int recepteur, long montant) {
		this.randNb = (new Random()).nextInt(MAX_VALUE);
		this.date = LocalDateTime.now().toString();
		this.emetteur = emetteur;
		this.recepteur = recepteur;
		this.montant = montant;
	}

	/**
	 * Constructeur manuel de transaction.
	 * <p>
	 * Utilisable uniquement au sein du package.
	 * Permet de créer une transaction avec une date et un montant fournis
	 * <p>
	 * @param date Date de la transaction
	 * @param emetteur Émetteur de la transaction
	 * @param recepteur Récepteur de la transaction
	 * @param montant Montant de la transaction en SatoBnB
	 * @param randNb Nombre aléatoire associé à la transaction
	 */
	protected Transaction(String date, int emetteur, int recepteur, long montant, int randNb) {
		this.randNb = randNb;
		this.date = date;
		this.emetteur = emetteur;
		this.recepteur = recepteur;
		this.montant = montant;
	}

	@Override
	public String toString() {
		return date + " - Source : user" + this.emetteur + " - Destination : user" + recepteur + " - Montant : "
				+ montant + " - " + randNb;
	}

	/**
	 * 
	 * @return une chaine de caractère contenant la date
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * Calcul le sha256 d'une transaction
	 * @return String contenant le hash
	 */
	public String calculateHash() {
		return HashUtil.applySha256(this.toString());
	}

}
