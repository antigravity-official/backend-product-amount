package antigravity.rds.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseMapper<T> {
    Long insert(T entity) throws Exception;
    int insert(String queryId, T entity) throws Exception;
    int update(T entity) throws Exception;
    int update(String queryId, T entity) throws Exception;
    int delete(T entity) throws Exception;
    int delete(String queryId, T entity) throws Exception;
    Optional<T> findOne(String queryId, Object conditions);
    Optional<T> findByPid(Long pid);
    Optional<List<T>> findList(String queryId, Object conditions);
    long total();
}
