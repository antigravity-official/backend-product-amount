package antigravity.mapper;

import antigravity.domain.entity.Promotion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PromotionProductMapper {

    @Select({
            "<script>"
            ,"SELECT p.id, p.promotion_type, p.name"
            ,"    , p.discount_type, p.discount_value"
            ,"    , p.use_started_at, p.use_ended_at"
            ,"FROM promotion p"
            ,"INNER JOIN promotion_products pp"
            ,"        ON pp.promotion_id = p.id"
            ,"WHERE pp.product_id = #{productId}"
            ,"  AND p.id IN "
            ,"      <foreach item='item' index='index' collection='couponIds' open='(' separator=',' close=')'>"
            ,"          #{item}"
            ,"      </foreach>"
            /*
             * 기간에 대한 정확한 정책 필요 (미만, 초과인지, 혹은 이하, 이상인지)
             * 우선 이하, 이상으로 조회
             */
            ,"  AND use_started_at &lt;= CURRENT_DATE() "
            ,"  AND use_ended_at &gt;= CURRENT_DATE() "
            ,"</script>"
    })
    List<Promotion> getPromotionsByProductId(int productId, int[] couponIds);
}