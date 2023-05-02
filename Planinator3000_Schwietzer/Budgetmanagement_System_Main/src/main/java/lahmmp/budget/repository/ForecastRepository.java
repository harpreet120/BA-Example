package lahmmp.budget.repository;

import lahmmp.budget.budgetManagement.Forecast;
import lahmmp.budget.userManagement.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository erbt vom JpaRepository und bietet hierdurch SQL Befehle an
@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {
}
