package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Category getById(int categoryId)
    {
        Category category = null;
        String getById = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getById)){

            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()){

                if (resultSet.next()){
                    int categoryIDFromDb = resultSet.getInt("category_id");
                    String nameFromDb = resultSet.getString("name");
                    String descriptionFromDb = resultSet.getString("description");
                    category = new Category(categoryIDFromDb, nameFromDb, descriptionFromDb);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
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
