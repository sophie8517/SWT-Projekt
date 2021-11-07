package kickstart.catalog;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bit {
	private int id2;
	private LocalDateTime date;
	private double einsatz;
	private Item item;
	private Status status;
	private Long id;


	public Bit(Item item, int id, LocalDateTime date, double einsatz){
		this.item = item;
		this.date = date;
		this.id2 = id;
		this.einsatz = einsatz;
		this.status = Status.OPEN;

	}

	public Bit() {

	}

	public void changeStatus(Status newStatus){
		this.status = newStatus;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@OneToOne
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getEinsatz() {
		return einsatz;
	}

	public void setEinsatz(double einsatz) {
		this.einsatz = einsatz;
	}

	public void setId(Long id2) {
		this.id = id2;
	}

	@Id
	public Long getId() {
		return id;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	@Id
	public int getId2() {
		return id2;
	}
}
