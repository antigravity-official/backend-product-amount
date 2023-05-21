package antigravity.repository;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import antigravity.domain.entity.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
@Profile("dev-jdbc")
public class ProductJdbcRepository implements ProductRepository {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Optional<Product> findById(Long id) {
		String query = "SELECT * FROM `product` WHERE id = :id ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		return   namedParameterJdbcTemplate.query(
			query,
			params,
			(rs, rowNum) -> Product.builder()
				.id(rs.getLong("id"))
				.name(rs.getString("name"))
				.price(rs.getInt("price"))
				.build()
		).stream().findAny();
	}
}
