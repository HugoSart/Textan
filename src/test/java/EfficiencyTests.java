import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import textan.model.I3EArticleParser;
import textan.model.article.Article;
import textan.model.article.processing.Analyser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: hugo_<br/>
 * Date: 01/07/2018<br/>
 * Time: 23:57<br/>
 */
public class EfficiencyTests {

    public static final List<Article> articles=  new ArrayList<>();
    public static final String[] tags = new String[]{"13", "17", "19", "20", "21", "22", "23", "29", "31", "46", "48", "49", "50", "98", "118", "120"};
    public static final String[] objectives = new String[]{
            "Building on this brief, our goal is to model the financial life of low-income individuals and find, via feed" +
                    "back controllers, the decisions he/she must make in order to cope with the three needs stated above.",
            "This paper presents a computational model which, with an intrinsic value input, generates market prices—" +
                    "unlike generic computational models (such as neural networks), the proposed model is built specific" +
                    "ally to illustrate market dynamics.",
            "The overarching aim of this work is therefore to determine the characteristics of financial indices related " +
                    "to financial stress, and to establish a robust metric for the extent of such ‘stress’. ",
            "With the aim of achieving high accuracies, preserving the interpretability, and managing un- certain and " +
                    "unbalanced data, this paper presents a novel method to deal with financial data classification by " +
                    "adopting type-2 fuzzy rule-based classifiers (FRBCs) generated from data by a multiob- jective " +
                    "evolutionary algorithm (MOEA).",
            "In this study, a multiobjective H2 /H∞ fuzzy investment is proposed for nonlinear stochastic jump diffusion " +
                    "financial systems to achieve the desired target with minimum investment cost and risk in Pareto optimal " +
                    "sense, simultaneously.",
            "This paper focuses on investigating the cross-border financial contagion based on a fuzzy dynamical system " +
                    "scenario simulation from a perspective of analyzing the volatility of interna- tional capital flows " +
                    "for a panel of 50 countries in emerging markets and advanced economies from 1980 to 2011.",
            "In this paper, combining the Gaussian process state-space model framework and the stochastic volatil- ity " +
                    "(SV) model, we introduce a new Gaussian process regression stochastic volatility (GPRSV) model building " +
                    "procedures for finan-cial time series data analysis and time-varying volatility modeling.",
            "This paper analyzes and conceptualizes the basic structures of the trading collusion in a wash trade by " +
                    "using a directed graph of traders.",
            "This paper develops a genetic bankrupt ratio analysis tool using a genetic algorithm to identify influencing " +
                    "ratios from different bankruptcy models and their influences in a quantitative form.",
            "The paper aims to determine the day-ahead market bidding strategies for retailers with flexible demands to " +
                    "maximize the short-term profit.",
            "The research described in this paper aims to accelerate the development and pricing of new derivatives in " +
                    "two different ways.",
            "This paper develops a novel approach to computation of the probability integrals encountered in " +
                    "derivative pricing using stochastic models estimated from historical data.",
            "This paper investigates the modeling of risk due to market and funding liquidity by capturing the joint " +
                    "dynamics of three time series: the treasury-Eurodollar spread, the VIX, and a metric derived from " +
                    "the S&P 500 spread.",
            "This paper is dedicated to European option pricing under assumption that the underlying asset follows a " +
                    "geometric Levy process.",
            "This paper introduces a novel extension of the technique for order-ing of preference by similarity to ideal " +
                    "solution (TOPSIS) method and uses fuzzy networks to solve multicriteria decision-making problems " +
                    "where both benefit and cost criteria are presented as subsystems.",
            "In this paper, we are interested in developing agent based models for studying global events in financial " +
                    "markets where the underlying value of the stock experiences a jump change (shock)."
    };

    public static void main(String[] args) throws IOException, ParseException {

        BasicConfigurator.configure();

        // Disable loggers
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
            logger.setLevel(Level.OFF);
        }

        System.out.println("Interpretando artigos...");
        loadArticles();

        float[] objMetrics = estimateObjectiveMetrics();
        System.out.println("Objetivo -> precision : " + objMetrics[0]);
        System.out.println("Objetivo -> recall    : " + objMetrics[1]);

    }

    public static void loadArticles() throws IOException, ParseException {
        for (String tag : tags)
            articles.add(new I3EArticleParser(PDDocument.load(new File("pdf/" + tag + ".pdf"))).toArticle());
    }

    public static float[] estimateObjectiveMetrics() {

        float[] ret = new float[2];

        int i = 0, recallHit = 0;
        for (Article article : articles) {
            Analyser analyser = new Analyser(article);
            String objective = analyser.findObjective().replaceAll("\\r?\\n", " ").replaceAll(" ", "");
            String desired = objectives[i].replaceAll("\\r?\\n", " ").replaceAll(" ", "");

            if (objective.charAt(objective.length() - 1) != '.')
                objective += ".";

            System.out.println("OBJECTIVE : " + objective);
            System.out.println("DESIRED   : " + desired);
            System.out.println("EQUALS    : " + objective.equals(desired));
            System.out.println();

            if (objective.equals(desired))
                recallHit++;

            i++;
        }

        int j = 0, precisionHit = 0;
        for (String objective : objectives) {

            Article article = new Article();
            article.abst = objective;
            Analyser analyser = new Analyser(article);

            String founded = analyser.findObjective();

            if (founded != null)
                precisionHit++;

            j++;

        }

        ret[1] = (float)recallHit / (float)i;
        ret[0] = (float)precisionHit / (float)j;

        return ret;
    }

    public static float[] estimateProblemMetrics() {
        float[] ret = new float[2];

        return ret;
    }

    public static float[] estimateMetodologyMetrics() {
        float[] ret = new float[2];

        return ret;
    }

    public static float[] estimateContributionMetrics() {
        float[] ret = new float[2];

        return ret;
    }

}
