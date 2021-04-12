package blockchainUtils;

import java.time.*;
import java.util.Random;

import miscUtils.HashUtil;

public class Transaction {

	static final int MAX_VALUE = 10000;

	private int randNb;

	private String date;

	private int emetteur;

	private int recepteur;

	private long montant;

	public Transaction(int emetteur, int recepteur, long montant) {
		this.randNb = (new Random()).nextInt(MAX_VALUE);
		this.date = LocalDateTime.now().toString();
		this.emetteur = emetteur;
		this.recepteur = recepteur;
		this.montant = montant;
	}

	protected Transaction(String date, int emetteur, int recepteur, long montant, int randNb) {
		this.randNb = randNb;
		this.date = date;
		this.emetteur = emetteur;
		this.recepteur = recepteur;
		this.montant = montant;
	}

	@Override
	public String toString() {
		return date + " - Source : user" + this.emetteur + " - Destination : user" + recepteur
				+ " - Montant : " + montant + " - " + randNb;
	}

	public String getDate() {
		return this.date;
	}

	public String calculateHash() {
		return HashUtil.applySha256(this.toString());
	}

}
