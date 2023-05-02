package lahmmp.budget.repository;

import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.budgetManagement.BudgetConnector;
import lahmmp.budget.userManagement.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository erbt vom JpaRepository und bietet hierdurch SQL Befehle an
@Repository
public interface BudgetRepository extends JpaRepository<BudgetConnector, Long> {
}
