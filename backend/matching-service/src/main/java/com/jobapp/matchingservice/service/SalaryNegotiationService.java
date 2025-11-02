package com.jobapp.matchingservice.service;

import com.jobapp.matchingservice.dto.SalaryNegotiation;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * AI-powered salary negotiation advisor
 * Helps candidates negotiate better offers
 */
@Service
public class SalaryNegotiationService {

    public SalaryNegotiation analyzeSalaryOffer(
            Double offeredSalary,
            List<String> skills,
            Integer yearsOfExperience,
            String location,
            String jobTitle) {

        // Calculate market value based on skills and experience
        Double marketValue = calculateMarketValue(skills, yearsOfExperience, location);
        
        // Determine if underpaid
        String verdict;
        Double percentageUnderpaid = 0.0;
        
        if (offeredSalary < marketValue * 0.85) {
            verdict = "UNDERPAID";
            percentageUnderpaid = ((marketValue - offeredSalary) / marketValue) * 100;
        } else if (offeredSalary > marketValue * 1.15) {
            verdict = "OVERPAID";
        } else {
            verdict = "FAIR";
        }

        // Calculate negotiation targets
        Double suggestedCounter = offeredSalary < marketValue ? 
                Math.min(marketValue * 1.15, offeredSalary * 1.25) : offeredSalary;
        Double minAcceptable = marketValue * 0.90;
        Double maxRealistic = marketValue * 1.20;

        // Generate negotiation strategy
        List<String> tips = generateNegotiationTips(verdict, percentageUnderpaid);
        List<String> justifications = generateJustifications(skills, yearsOfExperience, marketValue);
        String emailTemplate = generateEmailTemplate(offeredSalary, suggestedCounter, justifications);
        String phoneScript = generatePhoneScript(offeredSalary, suggestedCounter);
        List<String> alternatives = generateAlternatives();
        
        // Calculate success probability
        Integer successProb = calculateSuccessProbability(verdict, percentageUnderpaid);
        
        // Generate recommendation
        String recommendation = generateRecommendation(verdict, successProb, offeredSalary, marketValue);

        return SalaryNegotiation.builder()
                .offeredSalary(offeredSalary)
                .marketValue(Math.round(marketValue * 100.0) / 100.0)
                .suggestedCounterOffer(Math.round(suggestedCounter * 100.0) / 100.0)
                .minAcceptable(Math.round(minAcceptable * 100.0) / 100.0)
                .maxRealistic(Math.round(maxRealistic * 100.0) / 100.0)
                .verdict(verdict)
                .percentageUnderpaid(Math.round(percentageUnderpaid * 10.0) / 10.0)
                .negotiationTips(tips)
                .justifications(justifications)
                .emailTemplate(emailTemplate)
                .phoneScript(phoneScript)
                .alternatives(alternatives)
                .successProbability(successProb)
                .recommendedAction(recommendation)
                .build();
    }

    private Double calculateMarketValue(List<String> skills, Integer experience, String location) {
        // Base salary
        double baseSalary = 80000.0;
        
        // Experience multiplier
        if (experience != null) {
            baseSalary += (experience * 8000); // $8k per year of experience
        }
        
        // Skill premiums
        Map<String, Double> skillPremiums = Map.of(
            "kubernetes", 15000.0,
            "aws", 18000.0,
            "machine learning", 25000.0,
            "system design", 20000.0,
            "react", 12000.0,
            "python", 10000.0,
            "java", 12000.0,
            "golang", 15000.0
        );
        
        if (skills != null) {
            for (String skill : skills) {
                String skillLower = skill.toLowerCase();
                baseSalary += skillPremiums.getOrDefault(skillLower, 2000.0);
            }
        }
        
        // Location adjustment
        if (location != null) {
            String locLower = location.toLowerCase();
            if (locLower.contains("san francisco") || locLower.contains("sf") || locLower.contains("bay area")) {
                baseSalary *= 1.3; // 30% higher in SF
            } else if (locLower.contains("new york") || locLower.contains("nyc")) {
                baseSalary *= 1.25; // 25% higher in NYC
            } else if (locLower.contains("seattle") || locLower.contains("austin")) {
                baseSalary *= 1.15; // 15% higher
            }
        }
        
        return baseSalary;
    }

    private List<String> generateNegotiationTips(String verdict, Double percentageUnderpaid) {
        List<String> tips = new ArrayList<>();
        
        if (verdict.equals("UNDERPAID")) {
            tips.add("💡 You're being underpaid by " + Math.round(percentageUnderpaid) + "% - definitely negotiate!");
            tips.add("📊 Use market data to justify your counter-offer");
            tips.add("🎯 Be confident - they want YOU");
            tips.add("⏰ Respond within 24-48 hours (shows interest but not desperation)");
            tips.add("📧 Negotiate via email first (gives you time to think)");
        } else if (verdict.equals("FAIR")) {
            tips.add("✅ The offer is fair for the market");
            tips.add("💰 You can still try for 5-10% more");
            tips.add("🎁 If they won't budge on salary, ask for sign-on bonus or extra PTO");
        } else {
            tips.add("🎉 Great offer! Above market value");
            tips.add("✅ Consider accepting or negotiate non-salary perks");
        }
        
        tips.add("🤝 Always be professional and grateful");
        tips.add("📝 Get everything in writing");
        
        return tips;
    }

    private List<String> generateJustifications(List<String> skills, Integer experience, Double marketValue) {
        List<String> justifications = new ArrayList<>();
        
        justifications.add("Market rate for my skill set: $" + Math.round(marketValue / 1000) + "k");
        
        if (skills != null && skills.size() > 8) {
            justifications.add("Broad technical expertise: " + skills.size() + " relevant skills");
        }
        
        if (experience != null && experience >= 5) {
            justifications.add("Proven track record: " + experience + " years of experience");
        }
        
        justifications.add("Industry research shows similar roles pay $" + 
                Math.round((marketValue * 0.9) / 1000) + "k-$" + 
                Math.round((marketValue * 1.1) / 1000) + "k");
        
        return justifications;
    }

    private String generateEmailTemplate(Double offered, Double counter, List<String> justifications) {
        return String.format(
            "Dear [Hiring Manager],\n\n" +
            "Thank you for the offer of $%,d. I'm very excited about the opportunity to join your team!\n\n" +
            "After careful consideration and market research, I'd like to discuss the base salary. " +
            "Based on my skills and experience, I was expecting a range closer to $%,d.\n\n" +
            "My research shows:\n" +
            "• %s\n" +
            "• %s\n\n" +
            "I'm confident I can bring significant value to the team. Would you be open to discussing $%,d?\n\n" +
            "I'm flexible and happy to discuss the complete compensation package. " +
            "Looking forward to your thoughts!\n\n" +
            "Best regards,\n[Your Name]",
            Math.round(offered),
            Math.round(counter),
            justifications.get(0),
            justifications.get(1),
            Math.round(counter)
        );
    }

    private String generatePhoneScript(Double offered, Double counter) {
        return String.format(
            "Thank you so much for the offer! I'm really excited about the role.\n\n" +
            "I wanted to discuss the salary component. The offered $%,d is a bit below what " +
            "I was expecting based on market research for my skills and experience.\n\n" +
            "I've seen similar roles in the $%,d range. Would there be flexibility to discuss " +
            "something closer to that number?\n\n" +
            "I'm very interested in joining the team and I'm confident I can bring great value. " +
            "What are your thoughts?",
            Math.round(offered),
            Math.round(counter)
        );
    }

    private List<String> generateAlternatives() {
        return List.of(
            "Sign-on bonus ($10k-$25k)",
            "Annual bonus percentage increase",
            "Additional vacation days (5-10 days)",
            "Remote work flexibility",
            "Professional development budget ($5k-$10k)",
            "Equity/stock options",
            "Earlier performance review (6 months vs 12)",
            "Relocation assistance",
            "Better health benefits",
            "Flexible working hours"
        );
    }

    private Integer calculateSuccessProbability(String verdict, Double percentageUnderpaid) {
        if (verdict.equals("OVERPAID")) {
            return 95; // Already great offer
        }
        
        if (verdict.equals("FAIR")) {
            return 70; // Room to negotiate
        }
        
        // Underpaid
        if (percentageUnderpaid > 20) {
            return 90; // Significantly underpaid, strong case
        } else if (percentageUnderpaid > 10) {
            return 80; // Moderately underpaid
        } else {
            return 65; // Slightly underpaid
        }
    }

    private String generateRecommendation(String verdict, Integer successProb, Double offered, Double marketValue) {
        if (verdict.equals("UNDERPAID")) {
            return String.format(
                "🎯 NEGOTIATE! You're being offered $%,d but market value is $%,d. " +
                "Counter with $%,d. Success probability: %d%%",
                Math.round(offered),
                Math.round(marketValue),
                Math.round(marketValue * 1.1),
                successProb
            );
        } else if (verdict.equals("FAIR")) {
            return String.format(
                "✅ Fair offer, but you can try for 5-10%% more. Counter with $%,d. " +
                "If they say no, accept the current offer. Success probability: %d%%",
                Math.round(offered * 1.08),
                successProb
            );
        } else {
            return "🎉 Excellent offer! Above market rate. Consider accepting or negotiate perks.";
        }
    }
}

