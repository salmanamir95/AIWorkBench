package com.aiworkbench.user.entities;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departments" ,indexes = {
    @Index(name = "idx_departments_cost_center_active", columnList = "cost_center_code"),
    @Index(name = "idx_departments_active_lookup", columnList = "is_deleted, name")
})
@SQLDelete(sql = "UPDATE departments SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FilterDef(name = "deletedDepartmentFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedDepartmentFilter", condition = "is_deleted = :isDeleted")
public class Departments extends BaseAuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "cost_center_code", unique = true, length = 50)
    private String costCenterCode;

}
