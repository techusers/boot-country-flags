package org.search.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.search.dto.Continent;
import org.search.dto.Country;
import org.search.dto.Flags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class FlagSearchDaoImpl implements FlagSearchDao {
	
	Logger logger = LoggerFactory.getLogger(FlagSearchDaoImpl.class);
			
	private static final String USER = "searchserviceuser";

	@Autowired
	JdbcTemplate template;
	
	//get continent by name
	public Continent getContinent(String continentname) {
		
		try {
			
			List<Continent> contlist = template.query(
					"select * from continent where name = ? ", 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement preparedStatement) throws SQLException {
				            preparedStatement.setString(1, continentname);
				         }
					}, 
					new ContinentMapper());
			
			if( (contlist == null ) || contlist.isEmpty()) {
				logger.info("Continent not found in DB : "+continentname);
				return null;
			}
			
			Continent c = contlist.get(0);
			List<Country> lc = findAllCountry(c.getContinentid());
			c.getCountries().addAll(lc);
			
			return c;
		} catch (Exception e) {
			logger.error("Error retrieving continents : ",e);
			return null;
		}
		
	}
	
	//get all countries by continent 
	public List<Country> findAllCountry(int continentid) {
		try {
			List<Country> c = template.query(
					 "select * from country where continentid = ? ", 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement preparedStatement) throws SQLException {
				            preparedStatement.setInt(1, continentid);
				         }
					},  
					new CountryMapper());
			
			return c;
		} catch (Exception e) {
			logger.error("Error retrieving country : ",e);
			return null;
		}
	}
	
	private List<Continent> findAllContinents() {
		try {
			List<Continent> c = template.query(
					"select * from continent", 
					new ContinentMapper());

			return c;
		} catch (Exception e) {
			logger.error("Error retrieving all continents : ",e);
			return null;
		}
	}
	
	//get country by name
	public Country getCountry(String countryname) {
		
		try {

			List<Country> clist = template.query(
					"select * from country where name = ? ", 
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement preparedStatement) throws SQLException {
				            preparedStatement.setString(1, countryname);
				         }
					},  
					new CountryMapper());
			
			if( (clist == null ) || clist.isEmpty()) {
				logger.info("Country not found in DB : "+countryname);
				return null;
			}
			return clist.get(0);
		} catch (Exception e) {
			logger.error("Error retrieving country : ",e);
			return null;
		}
		
	}
	
	private Continent addContinent(final Continent c) {
		
		try {
			final String sql = "insert into continent (name,addedby, addeddate, modifiedby,modifieddate) "
					+ "values(?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			
			template.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, c.getContinent());
					ps.setString(2, USER);
					ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setString(4, USER);
					ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
					
					return ps;
				}
			}, holder);
			
			c.setContinentid(holder.getKey().intValue());
		} catch(Exception e) {
			logger.error("Error adding continent ",e);
		}
		return c;
	}
	
	private Country addCountry(final Country c) {
		try {
			final String sql = "insert into country (name, flag, continentid, addedby, "
					+ "addeddate, modifiedby,modifieddate) "
					+ "values(?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			
			template.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, c.getName());
					ps.setString(2, c.getFlag());
					ps.setInt(3, c.getContinentid());
					ps.setString(4, USER);
					ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setString(6, USER);
					ps.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
					
					return ps;
				}
			}, holder);
			
			c.setCountryid(holder.getKey().intValue());
		} catch(Exception e) {
			logger.error("Error adding country",e);
		}
		return c;
	}
	
	public boolean addContinents(Flags f) {
		
		if(f != null) {
			for(Continent cont : f.getContinents()) {
				addContinent(cont);
				
				for(Country c:cont.getCountries()) {
					c.setContinentid(cont.getContinentid());
					addCountry(c);
				}
			}
		}
		return true;
	}
	
	public Flags getFlags() {
		
		logger.info("Retrieving all flags");
		Flags flags = new Flags();
		
		List<Continent> lcont = findAllContinents();
		
		for(Continent cont: lcont) {
			List<Country> lc = findAllCountry(cont.getContinentid());
			cont.getCountries().addAll(lc);
		}
		
		flags.getContinents().addAll(lcont);
		return flags;
	}
}

class CountryMapper implements RowMapper<Country> {

	@Override
	public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
		Country c = new Country();
		c.setName(rs.getString("name"));
		c.setFlag(rs.getString("flag"));
		
		return c;
	}

}

class ContinentMapper implements RowMapper<Continent> {

	@Override
	public Continent mapRow(ResultSet rs, int rowNum) throws SQLException {
		Continent c = new Continent();
		c.setContinent(rs.getString("name"));
		c.setContinentid(rs.getInt("continentid"));
		
		return c;
	}

}

