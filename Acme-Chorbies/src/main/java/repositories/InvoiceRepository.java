
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	@Query("select i from Invoice i where i.openPeriod = ?1 and i.endPeriod = ?2 and i.chorbi.id = ?3")
	public Invoice findInvoiceByParams(Date openPeriod, Date endPeriod, int chorbiId);

	@Query("select i from Invoice i where i.openPeriod = (select max(i2.openPeriod) from Invoice i2 where i2.chorbi.id = ?1) and i.chorbi.id = ?1")
	public Invoice findLastInvoiceByChorbi(int chorbiId);

	@Query("select i from Invoice i where i.openPeriod > ?1 and i.endPeriod < ?2")
	public Collection<Invoice> getInvoiceByParamsAdmin(Date openPeriod, Date endPeriod);

	@Query("select i from Invoice i where i.openPeriod > ?1 and i.endPeriod < ?2 and i.chorbi.id = ?3")
	public Collection<Invoice> getInvoiceByParamsChorbi(Date openPeriod, Date endPeriod, int chorbiId);
}
