package com.example.vocab.data.local

import com.example.vocab.data.model.VocabCategories
import com.example.vocab.data.model.VocabWord

object DefaultVocabulary {
    val sampleWords: List<VocabWord> = listOf(
        // ==========================================
        // 1. TECHNICAL TERMS
        // ==========================================
        VocabWord(
            word = "Variable",
            phonetic = "/ˈveə.ri.ə.bəl/",
            partOfSpeech = "noun",
            meaning = "A named container that holds a value or data reference which can change during program execution.",
            example = "Declare a variable named counter to keep track of user swipe interactions.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Loop",
            phonetic = "/luːp/",
            partOfSpeech = "noun",
            meaning = "A sequence of instructions that is continuously repeated until a specific condition is reached or satisfied.",
            example = "The infinite scroll uses a continuous loop to cycle through vocabulary cards seamlessly.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Function",
            phonetic = "/ˈfʌŋk.ʃən/",
            partOfSpeech = "noun",
            meaning = "A reusable, modular block of code that takes inputs, performs a defined task, and optionally returns a value.",
            example = "The compose function renders the word definitions responsively on any screen size.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Database",
            phonetic = "/ˈdeɪ.tə.beɪs/",
            partOfSpeech = "noun",
            meaning = "A structured collection of data stored electronically in a computer system for fast search and retrieval.",
            example = "The app persists words and user progress in a local Room database.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Compiler",
            phonetic = "/kəmˈpaɪ.lər/",
            partOfSpeech = "noun",
            meaning = "A software program that translates human-readable source code into machine code for computer execution.",
            example = "The Kotlin compiler converts our modern syntax into efficient JVM bytecode.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Algorithm",
            phonetic = "/ˈæl.ɡə.rɪ.ðəm/",
            partOfSpeech = "noun",
            meaning = "A well-defined step-by-step procedure or set of logical rules for solving a problem or performing a computation.",
            example = "Social media feeds use a recommendation algorithm to deliver personalized content feeds.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Recursion",
            phonetic = "/rɪˈkɜː.ʒən/",
            partOfSpeech = "noun",
            meaning = "A programming technique where a function solves a problem by calling a smaller instance of itself until a base case.",
            example = "Tree directory traversals are naturally expressed using concise recursion.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Cache",
            phonetic = "/kæʃ/",
            partOfSpeech = "noun",
            meaning = "A high-speed temporary storage layer that retains copies of frequently accessed data to minimize latency.",
            example = "Audio pronunciations are stored in memory cache to enable instant playback without delay.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Latency",
            phonetic = "/ˈleɪ.tən.si/",
            partOfSpeech = "noun",
            meaning = "The time delay between a user action or system trigger and the resulting response from a computer or network.",
            example = "Low latency ensures audio pronunciation plays immediately upon tapping the speaker icon.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Encryption",
            phonetic = "/ɪnˈkrɪp.ʃən/",
            partOfSpeech = "noun",
            meaning = "The process of converting readable information into unreadable ciphertext to prevent unauthorized access.",
            example = "End-to-end encryption guarantees that private messages remain confidential.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Concurrency",
            phonetic = "/kənˈkʌr.ən.si/",
            partOfSpeech = "noun",
            meaning = "The ability of different parts or units of a program to execute out-of-order or in partial order without affecting the final outcome.",
            example = "Kotlin coroutines provide structured concurrency for asynchronous background database operations.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Polymorphism",
            phonetic = "/ˌpɒl.iˈmɔː.fɪ.zəm/",
            partOfSpeech = "noun",
            meaning = "The concept in object-oriented programming that allows entities of different types to be treated through a uniform interface.",
            example = "Polymorphism enables the UI rendering engine to draw various badge types through a unified contract.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Idempotent",
            phonetic = "/ˌaɪ.dɛmˈpoʊ.tənt/",
            partOfSpeech = "adjective",
            meaning = "An operation that produces the exact same result no matter how many times it is executed with the same input.",
            example = "HTTP PUT and DELETE requests are designed to be idempotent in REST architecture.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Deadlock",
            phonetic = "/ˈded.lɒk/",
            partOfSpeech = "noun",
            meaning = "A situation in concurrent programming where two or more threads are unable to proceed because each is waiting for the other to release a lock.",
            example = "Careful resource ordering was implemented to prevent database deadlocks under heavy load.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Sharding",
            phonetic = "/ˈʃɑː.dɪŋ/",
            partOfSpeech = "noun",
            meaning = "A database architecture pattern where large datasets are partitioned horizontally across multiple server instances.",
            example = "The cloud database implements sharding to distribute billions of vocabulary records across clusters.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Microservices",
            phonetic = "/ˈmaɪ.krəʊˌsɜː.vɪ.sɪz/",
            partOfSpeech = "noun",
            meaning = "An architectural style that structures an application as a collection of small, autonomous services modeled around a business domain.",
            example = "Migrating from a monolith to microservices allowed individual teams to deploy updates independently.",
            category = VocabCategories.TECHNICAL,
            difficulty = "Intermediate"
        ),

        // ==========================================
        // 2. MEDICAL TERMS
        // ==========================================
        VocabWord(
            word = "Symptom",
            phonetic = "/ˈsɪmp.təm/",
            partOfSpeech = "noun",
            meaning = "A physical or mental feature that is regarded as indicating a condition of disease, particularly such a feature that is apparent to the patient.",
            example = "Fatigue and a mild cough were the earliest symptoms reported by the patient.",
            category = VocabCategories.MEDICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Diagnosis",
            phonetic = "/ˌdaɪ.əɡˈnəʊ.sɪs/",
            partOfSpeech = "noun",
            meaning = "The identification of the nature of an illness or other problem by examination of the symptoms and medical tests.",
            example = "An accurate diagnosis was confirmed after evaluating blood panels and imaging scans.",
            category = VocabCategories.MEDICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Antibiotic",
            phonetic = "/ˌæn.ti.baɪˈɒt.ɪk/",
            partOfSpeech = "noun",
            meaning = "A medicine that inhibits the growth of or destroys microorganisms, especially bacteria.",
            example = "The doctor prescribed a broad-spectrum antibiotic to treat the bacterial chest infection.",
            category = VocabCategories.MEDICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Immunity",
            phonetic = "/ɪˈmjuː.nə.ti/",
            partOfSpeech = "noun",
            meaning = "The capability of multicellular organisms to resist harmful microorganisms and toxins through immune response.",
            example = "Childhood vaccinations provide long-lasting immunity against many infectious diseases.",
            category = VocabCategories.MEDICAL,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Biopsy",
            phonetic = "/ˈbaɪ.ɒp.si/",
            partOfSpeech = "noun",
            meaning = "An examination of tissue removed from a living body to discover the presence, cause, or extent of a disease.",
            example = "The surgeon performed a needle biopsy to determine whether the nodule was benign.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Hypertension",
            phonetic = "/ˌhaɪ.pəˈten.ʃən/",
            partOfSpeech = "noun",
            meaning = "Abnormally high blood pressure that forces arterial walls, increasing cardiovascular risks.",
            example = "Daily aerobic exercise and reduced sodium intake help mitigate chronic hypertension.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Prognosis",
            phonetic = "/prɒɡˈnəʊ.sɪs/",
            partOfSpeech = "noun",
            meaning = "The likely course or probable outcome of a medical condition or disease over time.",
            example = "With early detection and modern targeted therapy, the patient's prognosis is exceptionally positive.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Remission",
            phonetic = "/rɪˈmɪʃ.ən/",
            partOfSpeech = "noun",
            meaning = "A temporary or permanent diminution of the severity of disease symptoms or disappearance of active disease signs.",
            example = "Following six months of chemotherapy, tests indicated complete clinical remission.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Chronic",
            phonetic = "/ˈkrɒn.ɪk/",
            partOfSpeech = "adjective",
            meaning = "Persisting for a long time or constantly recurring, usually describing long-term illnesses.",
            example = "Type 2 diabetes is a chronic condition that requires consistent blood sugar management.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Idiopathic",
            phonetic = "/ˌɪd.i.əˈpæθ.ɪk/",
            partOfSpeech = "adjective",
            meaning = "Relating to or denoting any disease or condition that arises spontaneously or for which the cause is unknown.",
            example = "The neurological tremors were classified as idiopathic after extensive tests ruled out secondary causes.",
            category = VocabCategories.MEDICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Prophylaxis",
            phonetic = "/ˌprɒf.ɪˈlæk.sɪs/",
            partOfSpeech = "noun",
            meaning = "Action taken to prevent disease, especially by specified preventive medical measures.",
            example = "Antimalarial medication was administered as prophylaxis prior to traveling into tropical regions.",
            category = VocabCategories.MEDICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Pathophysiology",
            phonetic = "/ˌpæθ.əʊˌfɪz.iˈɒl.ə.dʒi/",
            partOfSpeech = "noun",
            meaning = "The disordered physiological processes associated with disease or injury.",
            example = "Understanding the pathophysiology of autoimmune disorders is key to developing biologic therapies.",
            category = VocabCategories.MEDICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Metastasis",
            phonetic = "/məˈtæs.tə.sɪs/",
            partOfSpeech = "noun",
            meaning = "The development of secondary malignant growths at a distance from a primary site of cancer.",
            example = "PET scans confirmed there was no evidence of metastasis to adjacent lymphatic organs.",
            category = VocabCategories.MEDICAL,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Sepsis",
            phonetic = "/ˈsep.sɪs/",
            partOfSpeech = "noun",
            meaning = "A life-threatening condition that arises when the body's response to an infection injures its own tissues and organs.",
            example = "Early recognition and rapid antibiotic delivery are critical in preventing septic shock.",
            category = VocabCategories.MEDICAL,
            difficulty = "Hard"
        ),

        // ==========================================
        // 3. BUSINESS TERMS
        // ==========================================
        VocabWord(
            word = "Revenue",
            phonetic = "/ˈrev.ən.juː/",
            partOfSpeech = "noun",
            meaning = "The total amount of money brought in by a company's operations, measured over a set amount of time.",
            example = "The software company reported a twenty percent increase in annual recurring revenue.",
            category = VocabCategories.BUSINESS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Asset",
            phonetic = "/ˈæs.et/",
            partOfSpeech = "noun",
            meaning = "An item of property owned by a person or company, regarded as having value and available to meet debts.",
            example = "Proprietary software and intellectual property represent the company's most valuable assets.",
            category = VocabCategories.BUSINESS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Capital",
            phonetic = "/ˈkæp.ɪ.təl/",
            partOfSpeech = "noun",
            meaning = "Wealth in the form of money or other assets owned by a person or organization or contributed for a purpose.",
            example = "The firm raised growth capital to expand operations into European markets.",
            category = VocabCategories.BUSINESS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Dividend",
            phonetic = "/ˈdɪv.ɪ.dend/",
            partOfSpeech = "noun",
            meaning = "A sum of money paid regularly by a company to its shareholders out of its profits.",
            example = "Long-term investors appreciated the steady quarterly dividend payments.",
            category = VocabCategories.BUSINESS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Leverage",
            phonetic = "/ˈliː.vər.ɪdʒ/",
            partOfSpeech = "noun",
            meaning = "The use of borrowed capital or financial debt to increase the potential return of an investment.",
            example = "While leverage can amplify profit during market upswings, it also increases financial downside risk.",
            category = VocabCategories.BUSINESS,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Liquidity",
            phonetic = "/lɪˈkwɪd.ə.ti/",
            partOfSpeech = "noun",
            meaning = "The efficiency or ease with which an asset or security can be converted into ready cash without affecting its market price.",
            example = "Treasury bills offer high liquidity compared to real estate holdings.",
            category = VocabCategories.BUSINESS,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Overhead",
            phonetic = "/ˈəʊ.və.hed/",
            partOfSpeech = "noun",
            meaning = "Ongoing business expenses not directly attributed to creating a product or service, such as rent and administrative utilities.",
            example = "Transitioning to remote work helped the enterprise significantly cut office overhead costs.",
            category = VocabCategories.BUSINESS,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Synergy",
            phonetic = "/ˈsɪn.ə.dʒi/",
            partOfSpeech = "noun",
            meaning = "The interaction or cooperation of two organizations to produce a combined effect greater than the sum of their separate efforts.",
            example = "The merger generated immense cost synergies across supply chain operations.",
            category = VocabCategories.BUSINESS,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Benchmark",
            phonetic = "/ˈbentʃ.mɑːk/",
            partOfSpeech = "noun",
            meaning = "A standard or point of reference against which other things may be compared or assessed.",
            example = "The S&P 500 serves as a primary benchmark for assessing portfolio performance.",
            category = VocabCategories.BUSINESS,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Solvency",
            phonetic = "/ˈsɒl.vən.si/",
            partOfSpeech = "noun",
            meaning = "The ability of a company to meet its long-term debts and financial obligations.",
            example = "Auditors confirmed the bank's strong solvency ratio despite volatile interest rates.",
            category = VocabCategories.BUSINESS,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Arbitrage",
            phonetic = "/ˈɑː.bɪ.trɑːʒ/",
            partOfSpeech = "noun",
            meaning = "The simultaneous purchase and sale of an asset in different markets to exploit minute price differentials for profit.",
            example = "Algorithmic trading desks execute currency arbitrage across global financial exchanges in milliseconds.",
            category = VocabCategories.BUSINESS,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Amortization",
            phonetic = "/əˌmɔː.tɪˈzeɪ.ʃən/",
            partOfSpeech = "noun",
            meaning = "The accounting practice of spreading an intangible asset's cost over its useful life, or paying off debt in regular installments.",
            example = "The financial statement outlined the amortization schedule for purchased software patents.",
            category = VocabCategories.BUSINESS,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Fiduciary",
            phonetic = "/fɪˈdjuː.ʃi.ə.ri/",
            partOfSpeech = "adjective",
            meaning = "Involving trust, especially with regard to the legal relationship between a trustee and a beneficiary.",
            example = "Investment advisers have a fiduciary duty to prioritize their clients' best financial interests.",
            category = VocabCategories.BUSINESS,
            difficulty = "Hard"
        ),

        // ==========================================
        // 4. STARTUP TERMS
        // ==========================================
        VocabWord(
            word = "Bootstrapping",
            phonetic = "/ˈbuːtˌstræp.ɪŋ/",
            partOfSpeech = "noun",
            meaning = "Building a company from the ground up with only personal savings and operational cash flow without external venture funding.",
            example = "They retained complete equity ownership by bootstrapping the software product for its initial three years.",
            category = VocabCategories.STARTUP,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Pitch",
            phonetic = "/pɪtʃ/",
            partOfSpeech = "noun",
            meaning = "A concise presentation given to potential investors or customers outlining a startup's product, market, and growth plan.",
            example = "The founders rehearsed their five-minute pitch ahead of Demo Day.",
            category = VocabCategories.STARTUP,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Freemium",
            phonetic = "/ˈfriː.mi.əm/",
            partOfSpeech = "noun",
            meaning = "A business model where basic services are provided free of charge while advanced or premium features require payment.",
            example = "The app utilizes a freemium model to attract millions of active learners before offering pro tiers.",
            category = VocabCategories.STARTUP,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "MVP",
            phonetic = "/ˌem.viːˈpiː/",
            partOfSpeech = "noun",
            meaning = "Minimum Viable Product; an initial version with just enough core features to validate hypotheses with early adopters.",
            example = "Shipping the MVP within two weeks gave the team instant user feedback on swipe gestures.",
            category = VocabCategories.STARTUP,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Pivot",
            phonetic = "/ˈpɪv.ət/",
            partOfSpeech = "verb",
            meaning = "To fundamentally shift a startup's business strategy, product focus, or target demographic after validating market realities.",
            example = "When user retention stalled, the company pivoted from an e-commerce platform into an educational tool.",
            category = VocabCategories.STARTUP,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Churn",
            phonetic = "/tʃɜːn/",
            partOfSpeech = "noun",
            meaning = "The percentage rate at which subscription customers cancel their service or stop using an application within a timeframe.",
            example = "Improving daily onboarding reduced monthly subscriber churn to under two percent.",
            category = VocabCategories.STARTUP,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Runway",
            phonetic = "/ˈrʌn.weɪ/",
            partOfSpeech = "noun",
            meaning = "The number of months a startup can survive before running out of cash, calculated from cash reserves and monthly burn rate.",
            example = "Closing the seed investment round extended the engineering team's financial runway by 18 months.",
            category = VocabCategories.STARTUP,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Traction",
            phonetic = "/ˈtræk.ʃən/",
            partOfSpeech = "noun",
            meaning = "Measurable evidence of market demand and customer adoption for a startup's product.",
            example = "Ten thousand daily active users demonstrated clear product traction to early-stage venture capitalists.",
            category = VocabCategories.STARTUP,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Unicorn",
            phonetic = "/ˈjuː.nɪ.kɔːn/",
            partOfSpeech = "noun",
            meaning = "A privately held startup company valued at over one billion US dollars.",
            example = "The AI analytics startup attained unicorn status after its latest Series C funding round.",
            category = VocabCategories.STARTUP,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Cap Table",
            phonetic = "/kæp ˈteɪ.bəl/",
            partOfSpeech = "noun",
            meaning = "Capitalization table; a detailed breakdown of a company's percentages of ownership, equity dilution, and value of shares.",
            example = "The legal counsel updated the cap table following the issuance of new employee stock option grants.",
            category = VocabCategories.STARTUP,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Cliff",
            phonetic = "/klɪf/",
            partOfSpeech = "noun",
            meaning = "A milestone period (commonly one year) that an employee or founder must work before any equity stock options begin to vest.",
            example = "Standard startup equity vesting schedules involve a four-year period with a one-year cliff.",
            category = VocabCategories.STARTUP,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Dilution",
            phonetic = "/daɪˈluː.ʃən/",
            partOfSpeech = "noun",
            meaning = "A reduction in the ownership percentage of a share of stock caused by the issuance of new stock.",
            example = "The founders accepted twenty percent equity dilution in exchange for tier-one venture backing.",
            category = VocabCategories.STARTUP,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "SAFE",
            phonetic = "/seɪf/",
            partOfSpeech = "noun",
            meaning = "Simple Agreement for Future Equity; a financial instrument created by Y Combinator allowing startups to raise money before setting valuation.",
            example = "The pre-seed round was closed efficiently using post-money SAFE agreements.",
            category = VocabCategories.STARTUP,
            difficulty = "Hard"
        ),

        // ==========================================
        // 5. GENERAL ENGLISH
        // ==========================================
        VocabWord(
            word = "Candid",
            phonetic = "/ˈkæn.dɪd/",
            partOfSpeech = "adjective",
            meaning = "Truthful, direct, and straightforward; frank and unreserved in speech.",
            example = "Her candid feedback on the prototype highlighted critical user experience flaws immediately.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Humble",
            phonetic = "/ˈhʌm.bəl/",
            partOfSpeech = "adjective",
            meaning = "Having or showing a modest or low estimate of one's own importance; not proud or arrogant.",
            example = "Despite his extraordinary success, he remained humble and eager to learn from newcomers.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Diligent",
            phonetic = "/ˈdɪl.ɪ.dʒənt/",
            partOfSpeech = "adjective",
            meaning = "Having or showing care and conscientiousness in one's work or duties.",
            example = "Through diligent daily practice, she mastered hundred of new vocabulary terms in a month.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Empathy",
            phonetic = "/ˈem.pə.θi/",
            partOfSpeech = "noun",
            meaning = "The ability to understand and share the feelings of another.",
            example = "Designing accessible interfaces requires deep empathy for users with diverse physical needs.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Benevolent",
            phonetic = "/bəˈnev.əl.ənt/",
            partOfSpeech = "adjective",
            meaning = "Well meaning, kindly, and motivated by a desire to promote the welfare of others.",
            example = "The benevolent patron funded scholarships for students from underrepresented communities.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Pragmatic",
            phonetic = "/præɡˈmæt.ɪk/",
            partOfSpeech = "adjective",
            meaning = "Dealing with things sensibly and realistically in a way that is based on practical rather than theoretical considerations.",
            example = "The engineers chose a pragmatic architecture that prioritized fast delivery over theoretical purity.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Resilience",
            phonetic = "/rɪˈzɪl.i.əns/",
            partOfSpeech = "noun",
            meaning = "The capacity to withstand or recover quickly from difficulties; toughness and elasticity.",
            example = "Developing mental resilience empowers individuals to navigate career pivots successfully.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Nuance",
            phonetic = "/ˈnjuː.ɑːns/",
            partOfSpeech = "noun",
            meaning = "A subtle distinction in or shade of meaning, expression, sound, or color.",
            example = "Mastering advanced English requires understanding cultural nuances in conversation.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Tenacious",
            phonetic = "/təˈneɪ.ʃəs/",
            partOfSpeech = "adjective",
            meaning = "Tending to keep a firm hold of something; clinging or adhering closely; persistent and determined.",
            example = "Her tenacious pursuit of the truth revealed systemic anomalies in the financial report.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Ubiquitous",
            phonetic = "/juːˈbɪk.wɪ.təs/",
            partOfSpeech = "adjective",
            meaning = "Present, appearing, or found everywhere simultaneously; omnipresent.",
            example = "Smartphones and high-speed wireless connectivity have become ubiquitous in contemporary society.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Ephemeral",
            phonetic = "/ɪˈfem.ər.əl/",
            partOfSpeech = "adjective",
            meaning = "Lasting for a very short time; transitory and fleeting.",
            example = "Trends on viral social feeds can be ephemeral, disappearing within a matter of days.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Voracious",
            phonetic = "/vəˈreɪ.ʃəs/",
            partOfSpeech = "adjective",
            meaning = "Wanting or devouring great quantities of food, or having a very eager approach to an activity such as reading.",
            example = "As a voracious reader, she finished several classic novels every week.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Quintessential",
            phonetic = "/ˌkwɪn.tɪˈsen.ʃəl/",
            partOfSpeech = "adjective",
            meaning = "Representing the most perfect or typical example of a quality or class.",
            example = "The cozy café served what locals considered the quintessential cup of morning espresso.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Hard"
        ),

        // ==========================================
        // 6. HISTORY TERMS
        // ==========================================
        VocabWord(
            word = "Dynasty",
            phonetic = "/ˈdɪn.ə.sti/",
            partOfSpeech = "noun",
            meaning = "A succession of people from the same family who play a prominent role in politics, business, or other fields.",
            example = "The Tang Dynasty is widely heralded as a golden age of cosmopolitan Chinese poetry and art.",
            category = VocabCategories.HISTORY,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Treaty",
            phonetic = "/ˈtriː.ti/",
            partOfSpeech = "noun",
            meaning = "A formally concluded and ratified agreement between sovereign states or nations.",
            example = "The peace treaty brought an end to years of territorial conflict between the neighboring countries.",
            category = VocabCategories.HISTORY,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Empire",
            phonetic = "/ˈem.paɪ.ər/",
            partOfSpeech = "noun",
            meaning = "An extensive group of states or countries under a single supreme authority, formerly especially an emperor or empress.",
            example = "The Roman Empire expanded infrastructure, legal codes, and trade routes across the Mediterranean.",
            category = VocabCategories.HISTORY,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Feudalism",
            phonetic = "/ˈfjuː.dəl.ɪ.zəm/",
            partOfSpeech = "noun",
            meaning = "The dominant social system in medieval Europe, in which nobles held lands from the Crown in exchange for military service.",
            example = "Under European feudalism, peasant serfs were obligated to cultivate land belonging to their local lord.",
            category = VocabCategories.HISTORY,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Imperialism",
            phonetic = "/ɪmˈpɪə.ri.ə.lɪ.zəm/",
            partOfSpeech = "noun",
            meaning = "A policy of extending a country's power and influence through diplomacy or military force.",
            example = "Nineteenth-century European imperialism reshaped political boundaries throughout Africa and Asia.",
            category = VocabCategories.HISTORY,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Sovereignty",
            phonetic = "/ˈsɒv.rɪn.ti/",
            partOfSpeech = "noun",
            meaning = "The supreme authority and power of a state or governing body to govern itself or another state.",
            example = "The newly independent nation defended its national sovereignty against external intervention.",
            category = VocabCategories.HISTORY,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Renaissance",
            phonetic = "/rəˈneɪ.səns/",
            partOfSpeech = "noun",
            meaning = "The revival of European art and literature under the influence of classical models in the 14th–16th centuries.",
            example = "The Renaissance fostered scientific inquiry, anatomical realism in painting, and philosophical humanism.",
            category = VocabCategories.HISTORY,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Suffrage",
            phonetic = "/ˈsʌf.rɪdʒ/",
            partOfSpeech = "noun",
            meaning = "The legal right to vote in political elections.",
            example = "Universal adult suffrage was secured after decades of relentless civil rights campaigns.",
            category = VocabCategories.HISTORY,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Hegemony",
            phonetic = "/hɪˈɡem.ə.ni/",
            partOfSpeech = "noun",
            meaning = "Leadership, dominion, or political predominance exercised by one state or social group over others.",
            example = "Sparta challenged the maritime hegemony of Athens during the protracted Peloponnesian War.",
            category = VocabCategories.HISTORY,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Historiography",
            phonetic = "/hɪˌstɒr.iˈɒɡ.rə.fi/",
            partOfSpeech = "noun",
            meaning = "The study of the writing of history and the methodologies through which historical knowledge is constructed over time.",
            example = "Modern historiography examines primary archival records to uncover voices often marginalized in classic texts.",
            category = VocabCategories.HISTORY,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Iconoclasm",
            phonetic = "/aɪˈkɒn.ə.klæz.əm/",
            partOfSpeech = "noun",
            meaning = "The destruction of religious images or sacred monuments; broadly, the action of attacking cherished beliefs or established traditions.",
            example = "The Byzantine empire experienced fierce theological debates during the era of iconoclasm.",
            category = VocabCategories.HISTORY,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Anachronism",
            phonetic = "/əˈnæk.rə.nɪ.zəm/",
            partOfSpeech = "noun",
            meaning = "A thing belonging or appropriate to a period other than that in which it exists, especially a thing that is conspicuously old-fashioned or premature.",
            example = "Spotting a wristwatch on a gladiator in the historical drama film was a humorous anachronism.",
            category = VocabCategories.HISTORY,
            difficulty = "Hard"
        ),

        // ==========================================
        // 7. DOCTOR & SPECIALIZED TERMS
        // ==========================================
        VocabWord(
            word = "Stethoscope",
            phonetic = "/ˈsteθ.ə.skəʊp/",
            partOfSpeech = "noun",
            meaning = "A medical instrument for listening to the action of someone's heart or breathing, typically having a small disc placed against the chest.",
            example = "The doctor placed the stethoscope on the patient's back to listen for abnormal bronchial wheezing.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Syringe",
            phonetic = "/sɪˈrɪndʒ/",
            partOfSpeech = "noun",
            meaning = "A tube with a nozzle and piston for sucking up and ejecting liquid in a thin stream, used for injecting drugs.",
            example = "The nurse filled the sterile syringe with the prescribed dose of pediatric vaccine.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Scalpel",
            phonetic = "/ˈskæl.pəl/",
            partOfSpeech = "noun",
            meaning = "A small, light, sharp-pointed knife used in surgical operations and autopsies.",
            example = "The lead surgeon requested a clean scalpel to begin the initial abdominal incision.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Catheter",
            phonetic = "/ˈkæθ.ɪ.tər/",
            partOfSpeech = "noun",
            meaning = "A flexible tube inserted through a narrow opening into a body cavity, particularly the bladder, for removing or introducing fluid.",
            example = "A Foley catheter was placed to monitor urinary output during post-operative intensive care.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Intubation",
            phonetic = "/ˌɪn.tʃuːˈbeɪ.ʃən/",
            partOfSpeech = "noun",
            meaning = "The insertion of a flexible endotracheal tube into the trachea to maintain an open airway or administer artificial ventilation.",
            example = "Emergency physicians performed emergency intubation to support the respiratory trauma victim.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Triage",
            phonetic = "/ˈtriː.ɑːʒ/",
            partOfSpeech = "noun",
            meaning = "The assignment of degrees of urgency to wounds or illnesses to decide the order of treatment of a large number of patients.",
            example = "During the mass casualty drill, emergency medical technicians established triage protocols swiftly.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Suture",
            phonetic = "/ˈsuː.tʃər/",
            partOfSpeech = "noun",
            meaning = "A stitch or row of stitches holding together the edges of a wound or surgical incision until natural healing occurs.",
            example = "The laceration on the forearm required six dissolvable sutures to close cleanly.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Endoscopy",
            phonetic = "/enˈdɒs.kə.pi/",
            partOfSpeech = "noun",
            meaning = "A procedure where an instrument introduced into the body gives an interior view of a hollow organ or cavity.",
            example = "An upper endoscopy verified the presence of a mild gastric ulcer in the duodenum.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Dialysis",
            phonetic = "/daɪˈæl.ə.sɪs/",
            partOfSpeech = "noun",
            meaning = "The clinical purification of blood by a machine, as a substitute for the normal function of failing kidneys.",
            example = "Patients with end-stage renal disease receive hemodialysis three times per week.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Sphygmomanometer",
            phonetic = "/ˌsfɪɡ.məʊ.məˈnɒm.ɪ.tər/",
            partOfSpeech = "noun",
            meaning = "An instrument for measuring blood pressure, typically consisting of an inflatable rubber cuff applied to the arm and a mercury column or gauge.",
            example = "The clinician calibrated the aneroid sphygmomanometer to ensure systolic readings remained accurate.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Auscultation",
            phonetic = "/ˌɔː.skəlˈteɪ.ʃən/",
            partOfSpeech = "noun",
            meaning = "The clinical practice of listening to internal sounds of the body, usually using a stethoscope, to examine the circulatory and respiratory systems.",
            example = "Careful cardiac auscultation revealed a subtle systolic murmur along the mitral valve.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Laparoscopy",
            phonetic = "/ˌlæp.əˈrɒs.kə.pi/",
            partOfSpeech = "noun",
            meaning = "A surgical procedure in which a fiber-optic instrument is inserted through the abdominal wall to view organs or permit minimally invasive surgery.",
            example = "Minimally invasive laparoscopy reduced patient hospital recovery time from weeks to merely days.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Ischemia",
            phonetic = "/ɪsˈkiː.mi.ə/",
            partOfSpeech = "noun",
            meaning = "An inadequate blood supply to an organ or part of the body, especially the heart muscles or cerebral brain tissue.",
            example = "Thrombolytic therapy was initiated rapidly to reverse myocardial ischemia before permanent tissue injury.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Hard"
        ),
        VocabWord(
            word = "Defibrillation",
            phonetic = "/diːˌfɪb.rɪˈleɪ.ʃən/",
            partOfSpeech = "noun",
            meaning = "The stopping of fibrillation of the heart by administering a controlled electric shock across the chest wall to restore normal rhythm.",
            example = "Automated external defibrillation within minutes of cardiac arrest dramatically increases survival chances.",
            category = VocabCategories.DOCTOR_SPECIALIZED,
            difficulty = "Hard"
        )
 ,

        // ==========================================
        // 8. CORPORATE TERMS (Strategy, PM, Phrasing, Finance, Negotiation, Tips)
        // ==========================================
        VocabWord(
            word = "Deliverables",
            phonetic = "/dɪˈlɪv.ər.ə.bəlz/",
            partOfSpeech = "noun",
            meaning = "Tangible or intangible products, services, or outcomes produced as a result of a project to be delivered to stakeholders.",
            example = "The technical project lead reviewed all sprint deliverables before the executive review.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "KPIs",
            phonetic = "/ˌkeɪ.piːˈaɪz/",
            partOfSpeech = "noun",
            meaning = "Key Performance Indicators; quantifiable measurements evaluated over time to gauge strategic and operational performance.",
            example = "Exceeding customer satisfaction KPIs led to quarterly company performance incentives.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Benchmarks",
            phonetic = "/ˈbentʃ.mɑːks/",
            partOfSpeech = "noun",
            meaning = "Standards or reference points against which performance, quality, and results are measured and compared across industry.",
            example = "Our cloud infrastructure latency benchmarks beat legacy competitors by forty percent.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Bandwidth",
            phonetic = "/ˈbænd.wɪtθ/",
            partOfSpeech = "noun",
            meaning = "The operational capacity, time, or mental availability needed to handle additional tasks or responsibilities.",
            example = "I would be glad to support the product launch, but my team lacks the bandwidth this sprint.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "ROI",
            phonetic = "/ˌɑːr.oʊˈaɪ/",
            partOfSpeech = "noun",
            meaning = "Return on Investment; a financial profitability metric measuring efficiency relative to the cost of an expenditure.",
            example = "Automating customer onboarding produced a measurable three-hundred percent ROI within six months.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Mitigation",
            phonetic = "/ˌmɪt.ɪˈɡeɪ.ʃən/",
            partOfSpeech = "noun",
            meaning = "The action of reducing the severity, impact, or likelihood of operational, technical, or financial risks.",
            example = "The risk committee approved an encrypted multi-cloud disaster mitigation protocol.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Circle Back",
            phonetic = "/ˈsɜː.kəl bæk/",
            partOfSpeech = "verb",
            meaning = "Professional business correspondence phrase meaning to return to a topic later; executive replacement for 'let's talk later'.",
            example = "Let's circle back during our next sync once we have reviewed the updated financial projections.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Per My Previous Email",
            phonetic = "/pɜːr maɪ ˈpriː.vi.əs ˈiː.meɪl/",
            partOfSpeech = "phrase",
            meaning = "A courteous business reminder directing the recipient's attention to details already communicated in earlier correspondence.",
            example = "Per my previous email, the revised milestone deliverables are documented in attachment B.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Take This Offline",
            phonetic = "/teɪk ðɪs ˌɒfˈlaɪn/",
            partOfSpeech = "phrase",
            meaning = "To continue a granular or tangent discussion outside of the current group meeting with only relevant parties.",
            example = "Let's take this architecture debate offline so we keep this cross-functional sync on schedule.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Scope Creep",
            phonetic = "/skoʊp kriːp/",
            partOfSpeech = "noun",
            meaning = "The uncontrolled, continuous addition of new features or requirements to a project without adjusting deadlines or budget.",
            example = "Strict milestone definitions were enforced to avoid costly scope creep during the redesign.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Stakeholder Alignment",
            phonetic = "/ˈsteɪkˌhəʊl.dər əˈlaɪn.mənt/",
            partOfSpeech = "noun",
            meaning = "The consensus, shared understanding, and active buy-in among all individuals who have an interest in a project's outcome.",
            example = "Securing stakeholder alignment early eliminated conflicting priorities during rollout.",
            category = VocabCategories.CORPORATE,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Contextual Phrase-Swapping",
            phonetic = "/kənˈtekstʃuəl freɪz ˈswɒp.ɪŋ/",
            partOfSpeech = "tip",
            meaning = "Actionable Learning Tip: Elevate professional speech by replacing casual phrasing with precise corporate idioms, like 'take this offline' instead of 'let's argue later'.",
            example = "Practice phrase-swapping during daily check-ins to make executive vocabulary second nature.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Corporate Publication Habit",
            phonetic = "/ˈkɔːr.pər.ət ˌpʌb.lɪˈkeɪ.ʃən ˈhæb.ɪt/",
            partOfSpeech = "tip",
            meaning = "Actionable Learning Tip: Read articles from Harvard Business Review, McKinsey Insights, or Financial Times to absorb strategic vocabulary in real journalism.",
            example = "Underline three unfamiliar business terms in each HBR article you read to build strategic acumen.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        ),

        // ==========================================
        // 9. IDIOMS
        // ==========================================
        VocabWord(
            word = "Bite The Bullet",
            phonetic = "/baɪt ðə ˈbʊl.ɪt/",
            partOfSpeech = "idiom",
            meaning = "To face a grim, inevitable situation with courage and stoicism after delaying it.",
            example = "After months of rising costs, the engineering team bit the bullet and refactored the legacy architecture.",
            category = VocabCategories.IDIOMS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Break The Ice",
            phonetic = "/breɪk ðə aɪs/",
            partOfSpeech = "idiom",
            meaning = "To do or say something that helps people feel more relaxed and comfortable in a social or professional setting.",
            example = "The moderator shared a humorous story to break the ice with new conference attendees.",
            category = VocabCategories.IDIOMS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Hit The Ground Running",
            phonetic = "/hɪt ðə ɡraʊnd ˈrʌn.ɪŋ/",
            partOfSpeech = "idiom",
            meaning = "To start an activity immediately with high energy, enthusiasm, and full efficiency.",
            example = "The new senior developer hit the ground running, closing five critical bugs in her first week.",
            category = VocabCategories.IDIOMS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "Elephant In The Room",
            phonetic = "/ˈel.ɪ.fənt ɪn ðə ruːm/",
            partOfSpeech = "idiom",
            meaning = "An obvious major problem or controversial issue that people avoid discussing because it is uncomfortable.",
            example = "The impending budget cuts were the elephant in the room that no director wanted to bring up.",
            category = VocabCategories.IDIOMS,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Low-Hanging Fruit",
            phonetic = "/loʊ ˈhæŋ.ɪŋ fruːt/",
            partOfSpeech = "idiom",
            meaning = "The most easily achieved goals or readily accessible opportunities in an initiative.",
            example = "Fixing the typography alignment on the sign-up page was low-hanging fruit for user conversion.",
            category = VocabCategories.IDIOMS,
            difficulty = "Easy"
        ),
        VocabWord(
            word = "See Eye To Eye",
            phonetic = "/siː aɪ tuː aɪ/",
            partOfSpeech = "idiom",
            meaning = "To agree fully with someone on a subject or strategic vision.",
            example = "The product lead and chief engineer see eye to eye on keeping the mobile interface minimal.",
            category = VocabCategories.IDIOMS,
            difficulty = "Easy"
        ),

        // ==========================================
        // 10. ONE-WORD SUBSTITUTIONS
        // ==========================================
        VocabWord(
            word = "Somnambulist",
            phonetic = "/sɒmˈnæm.bjʊ.lɪst/",
            partOfSpeech = "noun",
            meaning = "One-Word Substitution: A person who walks around or performs actions while asleep.",
            example = "The sleep medicine clinic monitored the somnambulist to ensure nocturnal safety.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Panacea",
            phonetic = "/ˌpæn.əˈsiː.ə/",
            partOfSpeech = "noun",
            meaning = "One-Word Substitution: A remedy or solution supposed to cure all diseases or solve all problems.",
            example = "While diet and exercise improve longevity, medicine has discovered no single panacea.",
            category = VocabCategories.MEDICAL,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Polyglot",
            phonetic = "/ˈpɒl.i.ɡlɒt/",
            partOfSpeech = "noun",
            meaning = "One-Word Substitution: A person who knows and can converse in several languages fluently.",
            example = "Being a polyglot enabled the ambassador to negotiate international trade accords smoothly.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Altruist",
            phonetic = "/ˈæl.tru.ɪst/",
            partOfSpeech = "noun",
            meaning = "One-Word Substitution: A person who shows unselfish devotion and practical concern for the welfare of others.",
            example = "As a committed altruist, she endowed community education scholarships for underprivileged youths.",
            category = VocabCategories.GENERAL_ENGLISH,
            difficulty = "Intermediate"
        ),
        VocabWord(
            word = "Whistleblower",
            phonetic = "/ˈwɪs.əlˌbləʊ.ər/",
            partOfSpeech = "noun",
            meaning = "One-Word Substitution: An employee or insider who exposes illegal, fraudulent, or unethical activity within an organization.",
            example = "The corporate whistleblower provided documentation that proved false accounting practices.",
            category = VocabCategories.CORPORATE,
            difficulty = "Easy"
        )

    )
}
