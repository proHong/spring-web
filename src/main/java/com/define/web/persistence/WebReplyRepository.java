package com.define.web.persistence;

import com.define.web.domain.WebBoard;
import com.define.web.domain.WebReply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebReplyRepository extends CrudRepository<WebReply, Long> {

    @Query("SELECT r FROM WebReply r WHERE r.board = ?1" + " AND r.rno > 0 ORDER BY r.rno ASC")
    public List<WebReply> getRepliesOfBoard(WebBoard board);
}
