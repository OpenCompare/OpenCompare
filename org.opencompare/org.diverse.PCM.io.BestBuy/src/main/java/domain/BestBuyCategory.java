package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BestBuyCategory is an immutable representation of a bestbuy product category
 * 
 * @author jmdavril
 */
public final class BestBuyCategory {

    private String id;
    private String name;
    private boolean active;
    private List<PathComponent> path;
    private List<SubCategory> subCategories;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public List<PathComponent> getPath() {
        if (this.path == null) {
            return null;
        }
        return Collections.unmodifiableList(this.path);
    }

    public List<SubCategory> getSubCategories() {
        if (this.subCategories == null) {
            return null;
        }
        return Collections.unmodifiableList(this.subCategories);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPath(List<PathComponent> path) {
        this.path = new ArrayList(path);
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = new ArrayList(subCategories);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Category={");
        builder.append(String.format("id=%s, name=%s", id, name));
        builder.append(", path={");
        for (PathComponent p : this.path) {
            builder.append(String.format("%s:%s/", p.getId(), p.getName()));
        }
        builder.append("}, sub-categories={");
        for (SubCategory sc : this.subCategories) {
            builder.append(String.format("(%s:%s)", sc.id, sc.name));
        }
        builder.append("}");
        return builder.toString();
    }

    public static final class PathComponent {

        private String id;
        private String name;

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("PathComponent=(%s:%s)", id, name);
        }
    }

    public static final class SubCategory {

        private String id;
        private String name;

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("SubCategory=(%s:%s)", id, name);
        }
    }
}
