package kirjanpito.db.sqlite;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kirjanpito.db.Entry;
import kirjanpito.db.sql.SQLEntryDAO;

/**
 * <code>SQLiteEntryDAO</code>:n avulla voidaan lisätä, muokata ja
 * poistaa vientejä sekä hakea olemassa olevien vientien
 * tietoja.
 *
 * @author Tommi Helineva
 */
public class SQLiteEntryDAO extends SQLEntryDAO {
	private SQLiteSession sess;

	public SQLiteEntryDAO(SQLiteSession sess) {
		this.sess = sess;
	}

	protected int getGeneratedKey() throws SQLException {
		return sess.getInsertId();
	}

	protected PreparedStatement getSelectByDocumentIdQuery() throws SQLException {
		return sess.prepareStatement("SELECT id, document_id, account_id, debit, amount, description, row_number, flags FROM entry WHERE document_id = ? ORDER BY row_number");
	}

	protected PreparedStatement getSelectByDocumentIdsQuery(String documentIds) throws SQLException {
		return sess.prepareStatement("SELECT id, document_id, account_id, debit, amount, description, row_number, flags FROM entry WHERE document_id IN (" + documentIds + ") ORDER BY row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByAccountAndNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN account a ON a.id = e.account_id INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY a.number, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByAccountAndDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN account a ON a.id = e.account_id INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY a.number, d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndAccountIdOrderByDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND e.account_id = ? ORDER BY d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndAccountIdOrderByNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND e.account_id = ? ORDER BY d.number, d.date, e.row_number");
	}

	protected PreparedStatement getSelectByAccountIdQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE e.account_id = ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.date >= ? AND d.date <= ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND d.date >= ? AND d.date <= ? ORDER BY d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndDateAndNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND d.date >= ? AND d.date <= ? AND d.number >= ? ORDER BY d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number, e.flags FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND d.number BETWEEN ? AND ? ORDER BY e.document_id, e.row_number");
	}

	protected PreparedStatement getInsertQuery() throws SQLException {
		return sess.prepareStatement("INSERT INTO entry (document_id, account_id, debit, amount, description, row_number, flags) VALUES (?, ?, ?, ?, ?, ?, ?)");
	}

	protected PreparedStatement getUpdateQuery() throws SQLException {
		return sess.prepareStatement("UPDATE entry SET document_id=?, account_id=?, debit=?, amount=?, description=?, row_number=?, flags=? WHERE id = ?");
	}

	protected PreparedStatement getDeleteQuery() throws SQLException {
		return sess.prepareStatement("DELETE FROM entry WHERE id = ?");
	}

	protected PreparedStatement getDeleteByPeriodIdQuery() throws SQLException {
		return sess.prepareStatement("DELETE FROM entry WHERE document_id IN (SELECT id FROM document WHERE period_id = ?)");
	}

	protected Entry createObject(ResultSet rs) throws SQLException {
		Entry obj = new Entry();
		obj.setId(rs.getInt(1));
		obj.setDocumentId(rs.getInt(2));
		obj.setAccountId(rs.getInt(3));
		obj.setDebit(rs.getBoolean(4));
		obj.setAmount(new BigDecimal(rs.getString(5)));
		obj.setDescription(rs.getString(6));
		obj.setRowNumber(rs.getInt(7));
		obj.setFlags(rs.getInt(8));
		return obj;
	}

	protected void setValuesToStatement(PreparedStatement stmt, Entry obj)
		throws SQLException
	{
		stmt.setInt(1, obj.getDocumentId());
		stmt.setInt(2, obj.getAccountId());
		stmt.setBoolean(3, obj.isDebit());
		stmt.setString(4, obj.getAmount().toString());
		stmt.setString(5, obj.getDescription());
		stmt.setInt(6, obj.getRowNumber());
		stmt.setInt(7, obj.getFlags());
	}
}
