package ru.brow.ModuleThreeApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.brow.ModuleThreeApplication.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
