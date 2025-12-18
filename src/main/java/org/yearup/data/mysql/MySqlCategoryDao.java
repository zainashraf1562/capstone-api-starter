package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();

        String getAll = "SELECT * FROM categories ";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getAll)){

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Category category = mapRow(resultSet);
                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String getById = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getById)) {

            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    Category category = mapRow(resultSet);
                    return category;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String create = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)){

            insertStatement.setString(1, category.getName());
            insertStatement.setString(2, category.getDescription());
            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to create Category!");
            }

            try (ResultSet resultSet = insertStatement.getGeneratedKeys()){
                if (resultSet.next()){
                    int resultId = resultSet.getInt(1);
                    category.setCategoryId(resultId);
                }else {
                    throw new SQLException("Failed, Error obtaining Id!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String update = """
                UPDATE categories
                SET name = ?, description = ?
                WHERE category_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(update)){

            updateStatement.setString(1, category.getName());
            updateStatement.setString(2, category.getDescription());
            updateStatement.setInt(3, categoryId);
            updateStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId)
    {
        String delete = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)){
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
