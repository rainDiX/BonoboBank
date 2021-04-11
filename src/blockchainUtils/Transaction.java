package blockchainUtils;
import java.time.*;
import java.util.Random;
import miscUtils.HashUtil;
public class Transaction {
	
	public Transaction(String emetteur, String recepteur, int montant) {
		Random rng = new Random();
		nb_random= rng.nextInt(MAX_VALUE);
		this.date = LocalDateTime.now().toString();
		this.emetteur = emetteur;
		this.recepteur = recepteur;
		this.montant = montant;
		
	}

	static final int MAX_VALUE=10000;
	
	private int nb_random;
	
	private String date;
	
	private String emetteur;
	
	private String recepteur;
	
	private int montant;

	@Override
	public String toString() {
		return this.emetteur+"-"+this.recepteur+":"+this.montant+"-"+this.nb_random;
	}

	public String getDate() {
		return this.date;
	}
	
	public String calculateHash() {
		return HashUtil.applySha256(this.toString());
	}
	
	
}
