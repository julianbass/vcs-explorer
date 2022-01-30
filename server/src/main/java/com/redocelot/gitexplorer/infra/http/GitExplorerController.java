package com.redocelot.gitexplorer.infra.http;

import java.util.ArrayList;

import com.redocelot.gitexplorer.components.commits.domain.Commit;
import com.redocelot.gitexplorer.components.commits.repos.CommitRepo;
import com.redocelot.gitexplorer.components.commits.repos.HttpCommitRepo;
import com.redocelot.gitexplorer.components.commits.usecases.GetCommitByShaInteractor;
import com.redocelot.gitexplorer.components.commits.usecases.GetCommitsInteractor;
import com.redocelot.gitexplorer.components.issues.domain.Issues;
import com.redocelot.gitexplorer.components.issues.repos.HttpIssuesRepo;
import com.redocelot.gitexplorer.components.issues.repos.IssuesRepo;
import com.redocelot.gitexplorer.components.issues.usecases.GetIssuesInteractor;
import com.redocelot.gitexplorer.components.metrics.domain.RepoMetrics;
import com.redocelot.gitexplorer.components.metrics.repo.HttpMetricsRepo;
import com.redocelot.gitexplorer.components.metrics.repo.MetricsRepo;
import com.redocelot.gitexplorer.components.metrics.usecases.GetRepoMetricsInteractor;
import com.redocelot.gitexplorer.components.sources.domain.Source;
import com.redocelot.gitexplorer.components.sources.repos.ISourceRepo;
import com.redocelot.gitexplorer.components.sources.repos.SourceRepoInMemory;
import com.redocelot.gitexplorer.components.sources.usecases.GetSources;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

@RestController
public class GitExplorerController {

    private ArrayList<Source> store;

    public GitExplorerController() {
        this.store = new ArrayList<Source>();
        this.store.add(new Source("octocat", "Hello-World"));
        this.store.add(new Source("ReactiveX", "RxJava"));
    }

    @RequestMapping(value = "/api/repos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Source> getRepos() {
        ISourceRepo repo = new SourceRepoInMemory(this.store);
        GetSources interactor = new GetSources(repo);
        return interactor.execute();
    }

    @RequestMapping(value = "/api/repos/{owner}/{repo}/commits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Commit> getCommits(@PathVariable(value = "owner") String owner,
            @PathVariable(value = "repo") String repo) {
        CommitRepo commitRepo = new HttpCommitRepo();
        GetCommitsInteractor interactor = new GetCommitsInteractor(commitRepo);
        return interactor.execute(owner, repo);

    }

    @RequestMapping(value = "/api/repos/{owner}/{repo}/commits/{sha}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Commit getCommit(@PathVariable(value = "owner") String owner, @PathVariable(value = "repo") String repo,
            @PathVariable(value = "sha") String sha) {
        CommitRepo commitRepo = new HttpCommitRepo();
        GetCommitByShaInteractor interactor = new GetCommitByShaInteractor(commitRepo);
        Commit commit = interactor.execute(owner, repo, sha);

        return commit;

    }

    @RequestMapping(value = "/api/repos/{owner}/{repo}/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public RepoMetrics getMetrics(@PathVariable(value = "owner") String owner,
            @PathVariable(value = "repo") String repo) {
        MetricsRepo metricsRepo = new HttpMetricsRepo();
        GetRepoMetricsInteractor interactor = new GetRepoMetricsInteractor(metricsRepo);
        RepoMetrics repoMetrics = interactor.execute(owner, repo);

        System.out.println(repo);
        return repoMetrics;

    }

    @RequestMapping(value = "/api/repos/{owner}/{repo}/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Issues> getIssues(@PathVariable(value = "owner") String owner,
            @PathVariable(value = "repo") String repo) {
        IssuesRepo commitRepo = new HttpIssuesRepo();
        GetIssuesInteractor interactor = new GetIssuesInteractor(commitRepo);
        return interactor.execute(owner, repo);

    }

    @PostMapping(path = "/api/repos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Source source) {
        
        ISourceRepo repo = new SourceRepoInMemory(this.store);
        repo.addSource(source);
        
        return;
    }

}