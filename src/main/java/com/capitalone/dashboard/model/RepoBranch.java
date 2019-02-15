package com.capitalone.dashboard.model;


import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class RepoBranch {
    private String url = "";
    private String branch = "";
    private RepoType type = RepoType.Unknown;

    public enum RepoType {
        SVN,
        GIT,
        Unknown;

        public static com.capitalone.dashboard.model.RepoBranch.RepoType fromString(String value) {
            if (value ==  null) return RepoType.Unknown;
            for (com.capitalone.dashboard.model.RepoBranch.RepoType repoType : values()) {
                if (repoType.toString().equalsIgnoreCase(value)) {
                    return repoType;
                }
            }
            throw new IllegalArgumentException(value + " is not a valid RepoType.");
        }
    }

    public RepoBranch(String url, String branch, RepoType repoType) {
        this.url = url;
        this.branch = branch;
        this.type = repoType;
    }

    public RepoBranch() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.toLowerCase(Locale.US);
    }

    public String getBranch() {
        switch (this.getType()) {
            case GIT: return getGITNormalizedBranch(branch);
            case SVN: return branch;
            default: return branch;
        }
    }

    public void setBranch(String branch) {
        switch (this.getType()) {
            case GIT: this.branch = getGITNormalizedBranch(branch); break;
            case SVN: this.branch = branch; break;
            default: this.branch = branch; break;
        }
    }

    public RepoType getType() {
        return type;
    }

    public void setType(RepoType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepoBranch that = (RepoBranch) o;

        return getRepoName().equals(that.getRepoName()) && getBranch().equals(that.getBranch());
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + getBranch().hashCode();
        return result;
    }

    private String getRepoName() {
        try {
            URL temp = new URL(url);
            return temp.getHost() + temp.getPath();
        } catch (MalformedURLException e) {
            return url;
        }

    }

    private String getGITNormalizedBranch (@NotNull String branch) {
        String[] tokens = branch.split("/");
        return tokens[tokens.length-1];
    }

}
