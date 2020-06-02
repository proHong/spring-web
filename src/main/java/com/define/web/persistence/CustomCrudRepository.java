package com.define.web.persistence;

import org.springframework.data.repository.CrudRepository;
import com.define.web.domain.WebBoard;

public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard {

}
