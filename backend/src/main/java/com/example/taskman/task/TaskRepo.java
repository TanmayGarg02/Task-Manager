package com.example.taskman.task;
import com.example.taskman.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
public interface TaskRepo extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
  Page<Task> findByAssignedTo(User user, Pageable pageable);
}
