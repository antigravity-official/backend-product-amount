package antigravity.mapper;

import antigravity.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {

    @Select({
            "SELECT *"
            ,"FROM product"
            ,"WHERE id = #{id}"
    })
    Product getProduct(int id);

}
