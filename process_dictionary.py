import json
import re

# 1. Load existing words
with open('app/src/main/assets/oxford_words.json', 'r', encoding='utf-8') as f:
    raw_words = json.load(f)

print(f"Loaded {len(raw_words)} initial words.")

# Keywords for comprehensive category mapping
tech_keywords = {
    'computer', 'software', 'hardware', 'code', 'data', 'digital', 'network',
    'internet', 'algorithm', 'system', 'processor', 'server', 'database', 'cyber',
    'interface', 'program', 'electronic', 'device', 'technology', 'technical',
    'sensor', 'circuit', 'robot', 'automation', 'virtual', 'signal', 'storage',
    'cloud', 'bandwidth', 'encryption', 'protocol', 'binary', 'pixel', 'router',
    'query', 'packet', 'terminal', 'logic', 'machine', 'microchip', 'browser',
    'firewall', 'kernel', 'developer', 'compiler', 'debugging', 'framework'
}

medical_keywords = {
    'disease', 'health', 'illness', 'medicine', 'medical', 'pain', 'patient',
    'treat', 'treatment', 'infection', 'virus', 'bacteria', 'cell', 'blood',
    'heart', 'brain', 'cancer', 'symptom', 'nurse', 'clinic', 'organ', 'genetic',
    'immune', 'chronic', 'fever', 'cough', 'injury', 'wound', 'tissue', 'bone',
    'muscle', 'artery', 'vein', 'liver', 'kidney', 'lung', 'digest', 'mental',
    'therapy', 'hygiene', 'allergy', 'antibiotic', 'vaccine', 'disorder', 'pulse',
    'breathing', 'syndrome', 'epidemic', 'diabetes', 'asthma', 'trauma', 'dosage'
}

doctor_keywords = {
    'doctor', 'surgeon', 'surgery', 'surgical', 'physician', 'pathology',
    'diagnosis', 'diagnostic', 'anesthesia', 'biopsy', 'radiology', 'oncology',
    'pediatric', 'cardiology', 'neurology', 'psychiatry', 'prescription',
    'pharmacy', 'pharmacology', 'incision', 'intravenous', 'stethoscope',
    'scalpel', 'autopsy', 'syringes', 'catheter', 'ultrasound', 'prognosis',
    'therapeutic', 'orthopedic', 'hematology', 'dermatology', 'obstetrics',
    'physiotherapy', 'resuscitation', 'endoscopy', 'dialysis', 'amputation',
    'rehabilitation', 'clinical', 'hospitalization', 'intensive care', 'paramedic'
}

business_keywords = {
    'finance', 'market', 'trade', 'economy', 'economic', 'business', 'profit',
    'revenue', 'tax', 'bank', 'invest', 'stock', 'share', 'fund', 'capital',
    'debt', 'loan', 'cost', 'price', 'audit', 'credit', 'currency', 'asset',
    'wealth', 'commerce', 'commercial', 'salary', 'wage', 'monopoly', 'fiscal',
    'consumer', 'corporation', 'dividend', 'inflation', 'merger', 'monetary',
    'portfolio', 'retail', 'wholesale', 'shareholder', 'transaction', 'balance sheet',
    'liability', 'equity', 'deficit', 'depreciation', 'interest rate', 'recession',
    'supply', 'demand', 'commodity', 'broker', 'enterprise', 'invoice', 'payroll'
}

startup_keywords = {
    'venture', 'founder', 'innovate', 'innovation', 'scale', 'pitch', 'launch',
    'agile', 'disrupt', 'incubate', 'product', 'growth', 'traction', 'seed',
    'angel', 'metric', 'strategy', 'bootstrapping', 'accelerator', 'lean',
    'pivot', 'unicorn', 'milestone', 'runway', 'burn rate', 'churn', 'mvp',
    'valuation', 'customer acquisition', 'incubator', 'prototype', 'roadmap',
    'retention', 'networking', 'ecosystem', 'scalable', 'sprint', 'scrum',
    'product-market fit', 'stakeholder', 'disruption', 'ideation', 'freemium',
    'synergy', 'early adopter', 'headcount', 'fundraising', 'term sheet', 'cap table'
}

history_keywords = {
    'century', 'ancient', 'empire', 'king', 'queen', 'war', 'battle', 'treaty',
    'historic', 'historical', 'history', 'dynasty', 'archaeology', 'medieval',
    'civilization', 'revolution', 'monarch', 'colonial', 'reign', 'conquest',
    'rebellion', 'crusade', 'renaissance', 'monument', 'archaeological', 'feudal',
    'sovereign', 'artifact', 'excavation', 'imperial', 'prehistoric', 'chronicle',
    'antiquity', 'gladiator', 'chivalry', 'settler', 'pharaoh', 'heir', 'heritage',
    'constitution', 'treaties', 'allies', 'armistice', 'parliament', 'decree'
}

# Specific words that must be Easy / Intermediate (NEVER Hard)
easy_force_words = {
    'accordingly', 'actually', 'basically', 'certainly', 'completely', 'definitely',
    'easily', 'entirely', 'especially', 'exactly', 'finally', 'frequently', 'generally',
    'greatly', 'hardly', 'immediately', 'increasingly', 'naturally', 'nearly', 'normally',
    'obviously', 'particularly', 'perfectly', 'possibly', 'previously', 'probably',
    'quickly', 'rarely', 'really', 'recently', 'regularly', 'relatively', 'roughly',
    'secondly', 'simply', 'slowly', 'slightly', 'sometimes', 'specifically', 'strongly',
    'successfully', 'suddenly', 'surely', 'totally', 'typically', 'ultimately', 'usually',
    'widely', 'wonderfully', 'ability', 'accept', 'accident', 'account', 'achieve',
    'across', 'action', 'active', 'activity', 'adapt', 'addition', 'address', 'admire',
    'admit', 'adult', 'advance', 'advantage', 'adventure', 'advice', 'afford', 'agree',
    'agreement', 'ahead', 'airline', 'airport', 'alarm', 'alcohol', 'alive', 'allow',
    'almost', 'alone', 'along', 'already', 'alright', 'although', 'always', 'amaze',
    'ambition', 'amount', 'analysis', 'analyze', 'ancient', 'angry', 'animal', 'announce',
    'annoy', 'answer', 'anxiety', 'anxious', 'anybody', 'anymore', 'anyone', 'anything',
    'anyway', 'anywhere', 'apart', 'apartment', 'apologize', 'apparent', 'appeal',
    'appear', 'appearance', 'application', 'apply', 'appoint', 'appreciate', 'approach',
    'appropriate', 'approval', 'approve', 'approximate', 'architect', 'area', 'argue',
    'argument', 'arise', 'arrange', 'arrangement', 'arrest', 'arrival', 'arrive',
    'article', 'artificial', 'artist', 'artistic', 'ashamed', 'aside', 'asleep',
    'aspect', 'assess', 'assessment', 'assign', 'assist', 'assistant', 'associate',
    'association', 'assume', 'assumption', 'assure', 'astonish', 'athlete', 'athletic',
    'atmosphere', 'attach', 'attack', 'attempt', 'attend', 'attention', 'attitude',
    'attract', 'attraction', 'attractive', 'attribute', 'audience', 'author',
    'authority', 'automatic', 'available', 'average', 'avoid', 'award', 'aware',
    'awareness', 'awful', 'awkward', 'balance', 'barrier', 'baseball', 'basement',
    'battery', 'battle', 'beauty', 'behave', 'behavior', 'belief', 'believe', 'belong',
    'beloved', 'beneath', 'benefit', 'besides', 'betray', 'bicycle', 'biology',
    'bitter', 'bizarre', 'blanket', 'bother', 'boundary', 'bravery', 'breathe',
    'brilliant', 'broadcast', 'brotherhood', 'bulletin', 'cabinet', 'calculate',
    'calendar', 'campaign', 'capacity', 'capture', 'careful', 'carrier', 'category',
    'cautious', 'celebrate', 'celebration', 'celebrity', 'cemetery', 'ceremony',
    'chairman', 'challenge', 'champion', 'championship', 'channel', 'chapter',
    'characteristic', 'chemical', 'chemistry', 'childhood', 'chimney', 'chocolate',
    'chronic', 'circuit', 'citizen', 'citizenship', 'civilian', 'clarify', 'classic',
    'classical', 'classify', 'classroom', 'climate', 'clothe', 'clothing', 'coastal',
    'coincide', 'collapse', 'colleague', 'collector', 'college', 'colonial', 'combine',
    'comfort', 'comfortable', 'command', 'commander', 'commerce', 'commercial',
    'commit', 'commitment', 'committee', 'commodity', 'communicate', 'communication',
    'community', 'companion', 'company', 'compare', 'comparison', 'compassion',
    'compatible', 'compel', 'compensate', 'compensation', 'compete', 'competent',
    'competition', 'competitive', 'competitor', 'compile', 'complain', 'complaint',
    'complete', 'complex', 'complexity', 'complicate', 'complicated', 'compliment',
    'component', 'compose', 'composer', 'composition', 'compound', 'comprehend',
    'comprehensive', 'comprise', 'compromise', 'compute', 'computer', 'conceal',
    'concede', 'conceive', 'concentrate', 'concentration', 'concept', 'conception',
    'concern', 'conclude', 'conclusion', 'concrete', 'condition', 'conduct', 'conference',
    'confess', 'confidence', 'confident', 'confine', 'confirm', 'confirmation',
    'conflict', 'conform', 'confront', 'confuse', 'confusion', 'congratulate',
    'connect', 'connection', 'conscious', 'consciousness', 'consensus', 'consent',
    'consequence', 'consequent', 'consequently', 'conservation', 'conservative',
    'consider', 'considerable', 'considerate', 'consideration', 'consist', 'consistent',
    'constant', 'constitute', 'constitution', 'construct', 'construction', 'consult',
    'consultant', 'consume', 'consumer', 'consumption', 'contact', 'contain', 'container',
    'contemplate', 'contemporary', 'content', 'contest', 'context', 'continent',
    'continual', 'continue', 'continuous', 'contract', 'contrast', 'contribute',
    'contribution', 'contributor', 'control', 'controversial', 'controversy',
    'convenience', 'convenient', 'convention', 'conventional', 'conversation',
    'convert', 'convey', 'convince', 'cooperate', 'cooperation', 'coordinate',
    'coordination', 'copper', 'corner', 'corporate', 'corporation', 'correct',
    'correlate', 'correspond', 'correspondence', 'corridor', 'costume', 'council',
    'counsel', 'counselor', 'countless', 'countryside', 'courage', 'courageous',
    'courtroom', 'courtesy', 'coverage', 'creation', 'creative', 'creativity',
    'creature', 'credibility', 'credible', 'criminal', 'criterion', 'critical',
    'criticism', 'criticize', 'critique', 'crocodile', 'crucial', 'culture',
    'cultural', 'curiosity', 'curious', 'currency', 'current', 'curtain', 'customer',
    'cylinder', 'daylight', 'daytime', 'deadline', 'dealership', 'debate', 'decade',
    'decent', 'decide', 'decision', 'decisive', 'declare', 'declaration', 'decline',
    'decorate', 'decoration', 'decorative', 'decrease', 'dedicate', 'dedication',
    'defeat', 'defend', 'defendant', 'defense', 'defensive', 'deficit', 'define',
    'definite', 'definition', 'definitive', 'degree', 'delay', 'delegate', 'delegation',
    'deliberate', 'delicate', 'delight', 'deliver', 'delivery', 'demand', 'democracy',
    'democratic', 'demonstrate', 'demonstration', 'density', 'department', 'departure',
    'depend', 'dependence', 'dependent', 'depict', 'deploy', 'deposit', 'depress',
    'depression', 'deprive', 'depth', 'deputy', 'derive', 'descend', 'describe',
    'description', 'desert', 'deserve', 'design', 'designate', 'designer', 'desirable',
    'desire', 'despair', 'desperate', 'despite', 'destination', 'destroy',
    'destruction', 'destructive', 'detail', 'detailed', 'detect', 'detection',
    'detective', 'deteriorate', 'determination', 'determine', 'develop', 'development',
    'device', 'devote', 'devotion', 'diagnose', 'diagnosis', 'dialect', 'dialogue',
    'diameter', 'diamond', 'dictate', 'differ', 'difference', 'different', 'difficult',
    'difficulty', 'digital', 'dignity', 'diligent', 'dimension', 'diminish', 'dinosaur',
    'diploma', 'diplomat', 'diplomatic', 'direct', 'direction', 'director', 'directory',
    'disable', 'disabled', 'disadvantage', 'disagree', 'disagreement', 'disappear',
    'disappoint', 'disappointment', 'disaster', 'disastrous', 'discern', 'discharge',
    'discipline', 'disclose', 'disclosure', 'discount', 'discourage', 'discourse',
    'discover', 'discovery', 'discreet', 'discrete', 'discriminate', 'discrimination',
    'discuss', 'discussion', 'disease', 'disguise', 'disgust', 'dishonest', 'dislike',
    'dismiss', 'dismissal', 'disorder', 'display', 'disposal', 'dispose', 'dispute',
    'disrupt', 'disruption', 'dissatisfaction', 'dissatisfied', 'distance', 'distant',
    'distinct', 'distinction', 'distinctive', 'distinguish', 'distort', 'distortion',
    'distract', 'distraction', 'distribute', 'distribution', 'distributor', 'district',
    'distrust', 'disturb', 'disturbance', 'diverse', 'diversity', 'divert', 'divide',
    'dividend', 'divine', 'division', 'divorce', 'doctrine', 'document', 'documentary',
    'documentation', 'domestic', 'dominant', 'dominate', 'domination', 'donation',
    'doorway', 'doubtful', 'downtown', 'downward', 'draft', 'drainage', 'drama',
    'dramatic', 'drawback', 'dreadful', 'drift', 'drought', 'duration', 'dynamic',
    'eager', 'earnest', 'earnings', 'earthquake', 'ecological', 'ecology', 'economic',
    'economical', 'economics', 'economy', 'ecosystem', 'editorial', 'educate',
    'education', 'educational', 'educator', 'effective', 'effectiveness', 'efficiency',
    'efficient', 'effort', 'elaborate', 'elderly', 'election', 'electric', 'electrical',
    'electrician', 'electricity', 'electronic', 'electronics', 'elegant', 'element',
    'elementary', 'elevate', 'elevation', 'elevator', 'eligible', 'eliminate',
    'elimination', 'elite', 'eloquent', 'elsewhere', 'embarrass', 'embarrassment',
    'embassy', 'embody', 'embrace', 'emerge', 'emergence', 'emergency', 'emission',
    'emotion', 'emotional', 'emphasis', 'emphasize', 'empire', 'empirical', 'employ',
    'employee', 'employer', 'employment', 'enable', 'enact', 'encounter', 'encourage',
    'encouragement', 'encyclopedia', 'endeavor', 'endless', 'endorse', 'endorsement',
    'endure', 'energetic', 'enforce', 'enforcement', 'engage', 'engagement', 'engine',
    'engineer', 'engineering', 'enhance', 'enjoyment', 'enormous', 'ensure',
    'enterprise', 'entertain', 'entertainer', 'entertainment', 'enthusiasm',
    'enthusiastic', 'entire', 'entity', 'entrance', 'entrepreneur', 'entry',
    'envelope', 'environment', 'environmental', 'envision', 'epidemic', 'episode',
    'equality', 'equation', 'equip', 'equipment', 'equivalent', 'erosion', 'error',
    'erupt', 'eruption', 'escalate', 'escape', 'escort', 'especially', 'essence',
    'essential', 'establish', 'establishment', 'estate', 'estimate', 'estimation',
    'eternal', 'ethical', 'ethics', 'ethnic', 'evaluate', 'evaluation', 'evaporate',
    'eventual', 'eventually', 'everyday', 'everyone', 'everything', 'everywhere',
    'evidence', 'evident', 'evolution', 'evolve', 'exact', 'exaggerate', 'examine',
    'examiner', 'example', 'exceed', 'excellence', 'excellent', 'exceptional',
    'excess', 'excessive', 'exchange', 'excite', 'excitement', 'exclude', 'exclusive',
    'excursion', 'execute', 'execution', 'executive', 'exempt', 'exemption', 'exercise',
    'exert', 'exhaust', 'exhaustion', 'exhibit', 'exhibition', 'exile', 'exist',
    'existence', 'existing', 'exit', 'exotic', 'expand', 'expansion', 'expect',
    'expectation', 'expedition', 'expel', 'expense', 'expensive', 'experience',
    'experiment', 'experimental', 'expert', 'expertise', 'expire', 'explain',
    'explanation', 'explicit', 'explode', 'exploit', 'exploration', 'explore',
    'explorer', 'explosion', 'explosive', 'export', 'expose', 'exposure', 'express',
    'expression', 'expressive', 'extend', 'extension', 'extensive', 'extent',
    'external', 'extinct', 'extinction', 'extraordinary', 'extreme', 'fabric',
    'fabulous', 'facility', 'factor', 'factory', 'faculty', 'failure', 'fairness',
    'faithful', 'familiar', 'familiarity', 'famous', 'fantastic', 'fantasy',
    'fascinate', 'fashion', 'fashionable', 'fatal', 'fatigue', 'fault', 'favorable',
    'favorite', 'feasible', 'feature', 'federal', 'feedback', 'fellowship', 'female',
    'feminine', 'festival', 'fidelity', 'fierce', 'filter', 'finance', 'financial',
    'financier', 'finish', 'finite', 'firefighter', 'firework', 'firmly', 'fiscal',
    'fitness', 'flexible', 'flight', 'flourish', 'fluid', 'focus', 'folklore',
    'footage', 'forecast', 'forehead', 'foreigner', 'foresee', 'forest', 'forever',
    'forgive', 'format', 'formation', 'formula', 'formulate', 'forthcoming',
    'fortress', 'fortunate', 'fortune', 'foster', 'foundation', 'founder', 'fraction',
    'fragile', 'fragment', 'fragrance', 'framework', 'franchise', 'fraternity',
    'freedom', 'freeway', 'frequency', 'frequent', 'friction', 'friendship',
    'frontline', 'frustrate', 'frustration', 'fuel', 'fulfill', 'fulfillment',
    'function', 'functional', 'fund', 'fundamental', 'funeral', 'furious', 'furniture',
    'furthermore', 'gallery', 'gamble', 'garbage', 'gardener', 'garment', 'gasoline',
    'gateway', 'gather', 'gathering', 'gender', 'general', 'generalize', 'generate',
    'generation', 'generator', 'generosity', 'generous', 'genetic', 'genius',
    'gentleman', 'genuine', 'geography', 'geology', 'geometry', 'gigantic', 'glance',
    'glimpse', 'global', 'globalization', 'globe', 'glorious', 'glory', 'governance',
    'government', 'governor', 'graceful', 'gracious', 'gradual', 'graduate',
    'graduation', 'grammar', 'grandchild', 'grandfather', 'grandmother', 'grandparent',
    'grant', 'grateful', 'gratitude', 'gravity', 'greenhouse', 'grief', 'grocery',
    'groundwork', 'growth', 'guarantee', 'guidance', 'guide', 'guideline', 'guilty',
    'habitat', 'habitual', 'hallway', 'handbook', 'handcraft', 'handful', 'handicap',
    'handsome', 'happiness', 'harbor', 'hardship', 'hardware', 'harmony', 'harvest',
    'hazardous', 'headache', 'headline', 'headquarters', 'heal', 'healthcare',
    'healthy', 'heartbeat', 'heartfelt', 'heartland', 'heavily', 'height', 'heritage',
    'heroic', 'heroine', 'hesitate', 'hesitation', 'hierarchy', 'highlight', 'highway',
    'historian', 'historic', 'historical', 'history', 'homeland', 'homeless',
    'honesty', 'honor', 'honorable', 'horizon', 'horizontal', 'hormone', 'horrible',
    'hospitable', 'hospital', 'hospitality', 'hostile', 'hostility', 'household',
    'housewife', 'humanity', 'humble', 'humiliate', 'humility', 'humorous', 'hurricane',
    'husband', 'hydrogen', 'hygiene', 'hypothesize', 'hysterical'
}

# Advanced / GRE / Hard vocabulary set
hard_words = {
    'aberration', 'abhor', 'abjure', 'abnegation', 'abrogate', 'abscond', 'abstruse',
    'acumen', 'admonish', 'adulation', 'alacrity', 'amalgamate', 'ameliorate',
    'anachronism', 'anachronistic', 'anomaly', 'antipathy', 'antithesis', 'apocryphal',
    'approbation', 'arcane', 'archaic', 'arduous', 'ascetic', 'assiduous', 'audacious',
    'austere', 'avarice', 'axiom', 'belligerent', 'benevolent', 'blandishment',
    'bombastic', 'cacophony', 'cajole', 'calumny', 'capricious', 'castigate',
    'catalyst', 'caustic', 'censure', 'chicanery', 'circumlocution', 'circumspect',
    'clandestine', 'coalesce', 'cogent', 'commensurate', 'compendium', 'complaisant',
    'concomitant', 'conflagration', 'connoisseur', 'contentious', 'conundrum',
    'corroborate', 'credulous', 'culpable', 'cupidity', 'cursory', 'dauntless',
    'dearth', 'debacle', 'decorum', 'deleterious', 'demagogue', 'demur', 'denigrate',
    'deprecate', 'deride', 'derisive', 'derivative', 'desiccate', 'desultory',
    'diatribe', 'didactic', 'diffident', 'dilatory', 'dilettante', 'disabuse',
    'discerning', 'discordant', 'discrepancy', 'discretionary', 'discursive',
    'disdain', 'disenfranchise', 'disingenuous', 'disparage', 'disparate',
    'dispassionate', 'dissemble', 'disseminate', 'dissonance', 'distend', 'divergent',
    'dogmatic', 'draconian', 'ebullient', 'eclectic', 'efficacy', 'effrontery',
    'egregious', 'elegy', 'elicit', 'eloquence', 'elucidate', 'emollient',
    'empirical', 'emulate', 'encomium', 'endemic', 'enervate', 'engender', 'enigma',
    'ephemeral', 'epistemic', 'epitome', 'equanimity', 'equivocate', 'erudite',
    'esoteric', 'eulogy', 'euphemism', 'evanescent', 'exacerbate', 'exculpate',
    'execrable', 'exigent', 'exonerate', 'expedient', 'expiate', 'explicate',
    'exponent', 'expurgate', 'extemporaneous', 'extol', 'extraneous', 'facetious',
    'fallacious', 'fastidious', 'fatuous', 'fawning', 'felicitous', 'fervid',
    'filibuster', 'florid', 'flout', 'foment', 'forestall', 'fractious', 'froward',
    'frugality', 'fulminate', 'furtive', 'gainsay', 'garrulous', 'gauche', 'germane',
    'glib', 'grandiloquent', 'gregarious', 'guile', 'hackneyed', 'hapless', 'harangue',
    'hegemony', 'hermetic', 'heterodox', 'heterogeneous', 'heuristic', 'histrionic',
    'hubris', 'hyperbole', 'iconoclast', 'idiosyncrasy', 'idiosyncratic', 'ignominious',
    'immutable', 'impecunious', 'imperious', 'impertinent', 'impervious', 'impetuous',
    'implacable', 'implicit', 'impugn', 'inadvertent', 'inchoate', 'incisive',
    'incongruous', 'incontrovertible', 'indefatigable', 'indolent', 'ineffable',
    'ineluctable', 'ineptitude', 'inert', 'inexorable', 'ingenuous', 'inimical',
    'iniquity', 'innocuous', 'inscrutable', 'insidious', 'insipid', 'insolent',
    'insouciant', 'intransigent', 'intrepid', 'inundate', 'inured', 'invective',
    'inveterate', 'invidious', 'irascible', 'irresolute', 'itinerant', 'jettison',
    'jocose', 'juggernaut', 'juxtaposition', 'laconic', 'lassitude', 'laudable',
    'legerdemain', 'levity', 'limpid', 'loquacious', 'lucid', 'lugubrious',
    'magnanimous', 'malevolent', 'malleable', 'maverick', 'mendacious', 'mercurial',
    'meticulous', 'militate', 'misanthrope', 'mitigate', 'modicum', 'mollify',
    'morose', 'multifarious', 'munificent', 'myopic', 'nadir', 'nascent', 'nebulous',
    'nefarious', 'neophyte', 'noisome', 'nonchalant', 'noxious', 'obdurate',
    'obfuscate', 'oblique', 'obsequious', 'obstinate', 'obstreperous', 'obviate',
    'occlude', 'officious', 'onerous', 'opaque', 'opprobrium', 'orthodox',
    'oscillate', 'ostentatious', 'ostracism', 'palatable', 'palliate', 'panacea',
    'panache', 'paragon', 'pariah', 'parsimonious', 'partisan', 'paucity', 'pedantic',
    'pejorative', 'penchant', 'penurious', 'peremptory', 'perennial', 'perfidious',
    'perfunctory', 'permeable', 'pernicious', 'perspicacious', 'pervade', 'pervasive',
    'petulant', 'phlegmatic', 'piety', 'pithy', 'placate', 'placid', 'platitude',
    'plebeian', 'plethora', 'polemic', 'pragmatic', 'precarious', 'precipitate',
    'precocious', 'predilection', 'preponderance', 'prescient', 'presumptuous',
    'prevaricate', 'probity', 'proclivity', 'prodigal', 'prodigious', 'profligate',
    'prolific', 'propensity', 'propitiate', 'propriety', 'prosaic', 'proscribe',
    'proselytize', 'protean', 'prudence', 'puerile', 'pugnacious', 'punctilious',
    'pungent', 'quaff', 'quagmire', 'quaint', 'quell', 'querulous', 'quiescent',
    'quixotic', 'quotidian', 'rancor', 'rapacious', 'rarefied', 'recalcitrant',
    'recant', 'recondite', 'redolent', 'refractory', 'refulgent', 'remonstrate',
    'renege', 'renounce', 'reprobate', 'repudiate', 'rescind', 'reticent',
    'ribald', 'rife', 'rudimentary', 'sagacious', 'salient', 'salutary', 'sanctimonious',
    'sanguine', 'satiate', 'scintillating', 'scrupulous', 'sedition', 'sensuous',
    'serendipity', 'servile', 'singular', 'sinuous', 'skepticism', 'solicitous',
    'somnolent', 'soporific', 'specious', 'spurious', 'staid', 'stolid', 'strident',
    'stupefy', 'subjugate', 'sublime', 'submissive', 'succinct', 'supercilious',
    'superfluous', 'surfeit', 'surreptitious', 'sycophant', 'tacit', 'taciturn',
    'tantamount', 'temerity', 'tenacious', 'tenuous', 'timorous', 'tirade', 'torpid',
    'tortuous', 'tractable', 'tranquility', 'transgression', 'transient', 'trenchant',
    'truculent', 'ubiquitous', 'umbrage', 'unctuous', 'undulate', 'unfeigned',
    'vacillate', 'variegated', 'venerable', 'venerate', 'veracity', 'verbose',
    'vexation', 'vicarious', 'vilify', 'vindicate', 'virulent', 'viscous',
    'vituperate', 'vociferous', 'voluble', 'voracious', 'warranted', 'wary',
    'winsome', 'wistful', 'zealot', 'zenith'
}

# 2. Gen Z words collection (120+ words)
gen_z_dataset = [
    {"w": "Rizz", "p": "/rɪz/", "t": "noun", "m": "Charm, charisma, or effortless romantic appeal and flirtation skills.", "e": "He had unmatched rizz and had everyone smiling in seconds.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "No Cap", "p": "/noʊ kæp/", "t": "phrase", "m": "No lie; complete honesty without exaggeration.", "e": "That was the best street taco in the entire city, no cap.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Cap", "p": "/kæp/", "t": "noun/verb", "m": "A lie or false statement; to speak untruthfully.", "e": "He said he met Drake yesterday, but everyone knew it was cap.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Bussin", "p": "/ˈbʌsɪn/", "t": "adjective", "m": "Extremely delicious, exceptionally good, or delightful (often food).", "e": "Her homemade mac and cheese is straight bussin.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Bet", "p": "/bɛt/", "t": "interjection", "m": "Agreement, confirmation, or affirmation; equivalent to 'deal' or 'sounds good'.", "e": "'Can you pick me up at eight?' 'Bet, see you then.'", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Drip", "p": "/drɪp/", "t": "noun", "m": "Stylish fashion sense, impressive designer outfit, or cool appearance.", "e": "His leather jacket and clean sneakers gave him immaculate drip.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Sus", "p": "/sʌs/", "t": "adjective", "m": "Suspicious, shady, untrustworthy, or doubtful.", "e": "He turned off his camera when the question was asked, which looked sus.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Mid", "p": "/mɪd/", "t": "adjective", "m": "Mediocre, underwhelming, or painfully average.", "e": "Everyone hyped up that burger place, but the meal was pretty mid.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Ghost", "p": "/ɡoʊst/", "t": "verb", "m": "To abruptly cut off all communication and messages with no explanation.", "e": "They talked every day for a month until he suddenly ghosted her.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Flex", "p": "/flɛks/", "t": "verb/noun", "m": "To show off accomplishments, wealth, or gear to impress people.", "e": "Showing his newly restored vintage sports car was an undeniable flex.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Slaps", "p": "/slæps/", "t": "verb", "m": "Is extraordinarily good, catchy, or impactful (especially a song).", "e": "Turn the car speakers up, this retro beat really slaps.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Based", "p": "/beɪst/", "t": "adjective", "m": "Being unapologetically authentic and honest without seeking validation.", "e": "Refusing to work unpaid overtime was a truly based move.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "GOAT", "p": "/ɡoʊt/", "t": "noun", "m": "Acronym for 'Greatest Of All Time' in any sport, art, or discipline.", "e": "Many tennis fans consider Serena Williams the undisputed GOAT.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Lowkey", "p": "/ˈloʊˌkiː/", "t": "adverb", "m": "Secretly, quietly, subtly, or to a moderate degree.", "e": "I lowkey hope it rains tomorrow so we can stay home and read.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Highkey", "p": "/ˈhaɪˌkiː/", "t": "adverb", "m": "Openly, intensely, passionately, or without holding back.", "e": "I highkey adore this song and have played it twenty times today.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Delulu", "p": "/dɪˈluːluː/", "t": "adjective", "m": "Playfully delusional, starry-eyed, or unrealistically hopeful.", "e": "Thinking a famous celebrity saw your story and smiled is peak delulu.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Solulu", "p": "/səˈluːluː/", "t": "noun", "m": "A humorous rhyming slang term meaning solution or answer.", "e": "Being delulu was her only solulu to surviving finals week.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Situationship", "p": "/ˌsɪtʃuˈeɪʃənʃɪp/", "t": "noun", "m": "A romantic or dating relationship that remains informal and undefined.", "e": "They spent months in a confusing situationship before making it official.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Cook", "p": "/kʊk/", "t": "verb", "m": "To perform brilliantly, execute a masterplan, or create magic.", "e": "Give him room to speak and let him cook, his strategy is genius.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Gyatt", "p": "/ɡjɑːt/", "t": "interjection", "m": "An exclamation expressing surprise, amazement, or awe.", "e": "He saw the enormous double-decker ice cream sundae and yelled 'gyatt!'.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Fanum Tax", "p": "/ˈfænəm tæks/", "t": "noun", "m": "The act of playfully taking food or snacks from a close friend.", "e": "My younger sister demanded a fanum tax and grabbed half my fries.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Skibidi", "p": "/ˈskɪbɪdi/", "t": "adjective", "m": "Viral surrealist internet meme phrase embodying absurdity and silliness.", "e": "The animated video went completely viral with skibidi dance clips.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Ohio", "p": "/oʊˈhaɪ.oʊ/", "t": "adjective", "m": "Internet meme descriptor for bizarre, eerie, surreal, or chaotic events.", "e": "A dancing raccoon at the subway station is purely an Ohio moment.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Sigma", "p": "/ˈsɪɡmə/", "t": "noun/adjective", "m": "A self-reliant, independent, and focused individual unbothered by social games.", "e": "He ignored the office drama and kept building in pure sigma mode.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Mewing", "p": "/ˈmjuːɪŋ/", "t": "noun", "m": "A tongue-posture habit intended to define facial structure.", "e": "He tapped his jaw and hushed his classmate because he was busy mewing.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Glow Up", "p": "/ˈɡloʊ ʌp/", "t": "noun/verb", "m": "A remarkable positive transformation in appearance, maturity, or confidence.", "e": "His confidence after starting college was an unbelievable glow up.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Caught In 4K", "p": "/kɔːt ɪn fɔːr keɪ/", "t": "phrase", "m": "Caught red-handed with crystal-clear, irrefutable digital evidence.", "e": "He claimed he wasn't eating cake, but he was caught in 4K on the baby monitor.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Touch Grass", "p": "/tʌtʃ ɡræs/", "t": "phrase", "m": "A reminder for someone obsessing over online debates to go outside into reality.", "e": "You've been arguing on forum threads all night, go touch grass.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Ratio", "p": "/ˈreɪʃioʊ/", "t": "noun/verb", "m": "When social media replies vastly outnumber likes, indicating broad disapproval.", "e": "His hot take on pineapple pizza earned an immediate, crushing ratio.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "NPC", "p": "/ˌɛn.piːˈsiː/", "t": "noun", "m": "Non-Player Character; someone behaving in an unthinking, repetitive, or robotic way.", "e": "He stood at the crosswalk repeating corporate buzzwords like an NPC.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Slay", "p": "/sleɪ/", "t": "verb", "m": "To do something with exceptional skill, excellence, or stunning beauty.", "e": "She wore an emerald silk suit and completely slayed the conference keynote.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Ate And Left No Crumbs", "p": "/eɪt ænd lɛft noʊ krʌmz/", "t": "phrase", "m": "Executed a flawless, breathtaking performance without a single flaw.", "e": "The Broadway actor hit every high note, ate and left no crumbs.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Brainrot", "p": "/ˈbreɪnrɒt/", "t": "noun", "m": "Mental lethargy caused by endlessly consuming mindless viral media.", "e": "Watching three hours of surreal 10-second looping reels gave me brainrot.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Mogging", "p": "/ˈmɒɡɪŋ/", "t": "verb", "m": "Completely outshining someone in posture, height, or jawline aesthetics.", "e": "He walked into the fitness expo unintentionally mogging the entire room.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Looksmaxxing", "p": "/ˈlʊksmæksɪŋ/", "t": "noun", "m": "The deliberate process of optimizing personal physical appearance and grooming.", "e": "Drinking water, fixing posture, and styling hair are basic looksmaxxing steps.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Rizzler", "p": "/ˈrɪzlər/", "t": "noun", "m": "A person known for smooth charisma, charm, and great conversation skills.", "e": "He chatted with the whole gallery and made everyone laugh; true rizzler.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Yap", "p": "/jæp/", "t": "verb", "m": "To talk continuously, ramble without substance, or chatter endlessly.", "e": "Our teammate spent the entire standup meeting yapping about lunch.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Yapper", "p": "/ˈjæpər/", "t": "noun", "m": "A person who talks endlessly or cannot stop narrating stories.", "e": "Don't sit next to him on the train unless you want a masterclass from a yapper.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "It's Giving", "p": "/ɪts ˈɡɪvɪŋ/", "t": "phrase", "m": "Radiating a distinct aesthetic, nostalgic feeling, or stylistic energy.", "e": "That moody dim café lighting is giving 1940s detective film noir.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Glazing", "p": "/ˈɡleɪzɪŋ/", "t": "verb", "m": "Excessively praising, complimenting, or kissing up to someone dishonestly.", "e": "Stop glazing the professor just because you turned the essay in early.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Crash Out", "p": "/kræʃ aʊt/", "t": "verb", "m": "To lose all composure, erupt in reckless temper, or self-sabotage.", "e": "He almost crashed out when his team blew a 20-point lead in two minutes.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Understood The Assignment", "p": "/ˌʌndərˈstʊd ðə əˈsaɪnmənt/", "t": "phrase", "m": "Performed a task with utmost creativity, elegance, and precision.", "e": "The costume designer for that period drama completely understood the assignment.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Main Character", "p": "/meɪn ˈkærɪktər/", "t": "noun", "m": "An individual behaving with infectious confidence as if starring in their own film.", "e": "Putting on headphones and walking briskly in the autumn mist gives main character energy.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Valid", "p": "/ˈvælɪd/", "t": "adjective", "m": "Legitimate, acceptable, understandable, or justified.", "e": "Taking an afternoon nap after running a 10K is totally valid.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Bop", "p": "/bɒp/", "t": "noun", "m": "An irresistibly catchy, energizing, and rhythmic musical track.", "e": "That new summer single is an indisputable bop.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Simp", "p": "/sɪmp/", "t": "noun/verb", "m": "Someone showing excessive, submissive devotion to someone who rarely reciprocates.", "e": "He drove four hours in heavy snow just to deliver her bubble tea like a simp.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Stan", "p": "/stæn/", "t": "noun/verb", "m": "An overly enthusiastic, loyal, and devoted fan or supporter of a creator or artist.", "e": "I stan that indie filmmaker because every single script is original.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Yeet", "p": "/jiːt/", "t": "verb/interjection", "m": "To throw or discard something with tremendous force, velocity, or enthusiasm.", "e": "He opened the window and yeeted the empty soda box into recycling.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Hits Different", "p": "/hɪts ˈdɪfərənt/", "t": "phrase", "m": "Feels uniquely powerful, deeper, or more memorable than anything else.", "e": "Drinking ice-cold lemonade after a long soccer match hits different.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Snatched", "p": "/snætʃt/", "t": "adjective", "m": "Looking impeccably sharp, well-tailored, and visually stunning.", "e": "Her tailored blazer cinched the waist and looked totally snatched.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Vibe Check", "p": "/vaɪb tʃɛk/", "t": "noun", "m": "An assessment of the emotional mood, sincerity, or collective energy of a group.", "e": "Before signing the new lease, the flatmates did an honest vibe check.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Living Rent Free", "p": "/ˈlɪvɪŋ rɛnt friː/", "t": "phrase", "m": "Occupying someone's persistent thoughts or memories without their control.", "e": "That catchy jingle from the coffee commercial is living rent free in my head.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Big L", "p": "/bɪɡ ɛl/", "t": "noun", "m": "A major defeat, disappointing failure, or embarrassing misstep.", "e": "Missing the flight because he forgot his passport was a big L.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Big W", "p": "/bɪɡ ˈdʌbəl.juː/", "t": "noun", "m": "A tremendous success, triumphant victory, or great achievement.", "e": "Negotiating a twenty percent salary raise was a monumental big W.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Periodt", "p": "/ˈpɪəri.ət/", "t": "interjection", "m": "Used at the end of a statement to declare it absolute final fact without debate.", "e": "Home cooking is always healthier than takeout, periodt.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Tea", "p": "/tiː/", "t": "noun", "m": "Juicy gossip, insider news, or sensational personal revelations.", "e": "Sit down on the couch and spill all the tea from yesterday's wedding.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Salty", "p": "/ˈsɔːlti/", "t": "adjective", "m": "Bitter, resentful, or unnecessarily upset over a minor loss or slight.", "e": "He was salty all afternoon because he lost the friendly chess match.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Clout", "p": "/klaʊt/", "t": "noun", "m": "Influence, popularity, online following, or social prominence.", "e": "He collaborated with trending streamers just to chase online clout.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Boujee", "p": "/ˈbuːʒi/", "t": "adjective", "m": "Luxurious, high-end, lavish, or aspiring to wealthy extravagance.", "e": "Ordering sparkling mineral water at dinner felt surprisingly boujee.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Extra", "p": "/ˈɛkstrə/", "t": "adjective", "m": "Over-the-top, dramatic, excessively theatrical, or unnecessarily flamboyant.", "e": "Wearing a tuxedo to a backyard barbecue is just a bit too extra.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Cheugy", "p": "/ˈtʃuːɡi/", "t": "adjective", "m": "Outdated, trying too hard to look trendy, or clinging to obsolete 2010s aesthetics.", "e": "Wearing wooden block signs that say 'Live Laugh Love' is universally considered cheugy.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Shook", "p": "/ʃʊk/", "t": "adjective", "m": "Deeply shocked, emotionally stunned, or rattled by an unexpected twist.", "e": "The mystery plot twist at the climax left everyone completely shook.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Finna", "p": "/ˈfɪnə/", "t": "auxiliary verb", "m": "Contracted form of 'fixing to'; about to, or preparing to do something soon.", "e": "I'm finna cook a big pot of spicy chicken pasta.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Gucci", "p": "/ˈɡuːtʃi/", "t": "adjective", "m": "All good, fine, well-handled, or problem-free.", "e": "'Don't worry about the spilled water, everything is totally gucci.'", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Lit", "p": "/lɪt/", "t": "adjective", "m": "High-energy, exciting, thrilling, or passionately lively.", "e": "The rooftop birthday celebration was absolutely lit from start to finish.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Deadass", "p": "/ˈdɛdˌæs/", "t": "adverb/adjective", "m": "Completely serious, truthful, without exaggeration, or solemn.", "e": "I'm deadass not lying, I saw a real bald eagle in the park today.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Facts", "p": "/fækts/", "t": "interjection", "m": "Expression of firm agreement indicating that what was said is absolute truth.", "e": "'Sleep is the best medicine.' 'Straight facts.'", "c": "Gen Z terms", "d": "Easy"},
    {"w": "FR FR", "p": "/fɔːr rɪəl/", "t": "phrase", "m": "Acronym for 'for real, for real', emphasizing earnest sincerity and emphasis.", "e": "This exam was the toughest one all semester, fr fr.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Ship", "p": "/ʃɪp/", "t": "verb", "m": "To endorse, root for, or support a romantic relationship between two people.", "e": "Ever since season one, the audience has shipped those two detectives.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Clapback", "p": "/ˈklæpbæk/", "t": "noun/verb", "m": "A swift, sharp, witty, and devastating verbal comeback to criticism.", "e": "Her polite clapback to the online critic shut down the whole debate.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Receipts", "p": "/rɪˈsiːts/", "t": "noun", "m": "Undeniable documented proof, screenshots, emails, or records verifying an assertion.", "e": "Before you accuse someone of breaking a promise, make sure you show the receipts.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Down Bad", "p": "/daʊn bæd/", "t": "phrase", "m": "In a state of desperate longing, lovesickness, or hopeless romantic obsession.", "e": "He texted her three times in fifteen minutes; the man is down bad.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Slept On", "p": "/slɛpt ɒn/", "t": "phrase", "m": "Underrated, overlooked, or not receiving the broad appreciation it deserves.", "e": "That independent acoustic record is so slept on by mainstream radio.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Spill The Tea", "p": "/spɪl ðə tiː/", "t": "phrase", "m": "To share exciting gossip, confidential details, or dramatic news.", "e": "Come on, sit down with your iced coffee and spill the tea.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Side Eye", "p": "/saɪd aɪ/", "t": "noun/verb", "m": "A sideways look expressing suspicion, skepticism, or subtle disapproval.", "e": "She gave him major side eye when he claimed he forgot his wallet again.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Bombastic Side Eye", "p": "/bɒmˈbæstɪk saɪd aɪ/", "t": "phrase", "m": "An exaggerated, dramatic sideways glare expressing profound judgment and amusement.", "e": "When he tried to cut the lunch line, the whole table gave him bombastic side eye.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Soft Launch", "p": "/sɒft lɔːntʃ/", "t": "verb/noun", "m": "Subtly hinting at a new relationship online without showing their face.", "e": "Posting two coffee cups and an unidentified hand was a classic soft launch.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Hard Launch", "p": "/hɑːrd lɔːntʃ/", "t": "verb/noun", "m": "Publicly announcing a relationship online in an unmistakable, prominent post.", "e": "They hard launched their wedding engagement with a photoshoot on Instagram.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Roman Empire", "p": "/ˈroʊmən ˈɛmpaɪər/", "t": "noun", "m": "A quirky, random topic or historic passion that someone thinks about constantly.", "e": "Deep-sea bioluminescence is honestly my personal Roman Empire.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Gaslight", "p": "/ˈɡæslaɪt/", "t": "verb", "m": "To manipulate someone psychologically into questioning their own sanity or perception.", "e": "He tried to gaslight his roommate into thinking the dirty dishes were hers.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Gatekeep", "p": "/ˈɡeɪtkiːp/", "t": "verb", "m": "To withhold knowledge, a cool spot, or music to keep it exclusive.", "e": "Don't gatekeep that cozy hidden noodle shop, tell us where it is!", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Girlboss", "p": "/ˈɡɜːrlbɒs/", "t": "noun/verb", "m": "A confident, ambitious woman dominating business or personal independence.", "e": "She launched three boutique stores before age twenty-five like a true girlboss.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Era", "p": "/ˈɪərə/", "t": "noun", "m": "A distinct phase or personal period characterized by a specific mindset or lifestyle.", "e": "I am officially entering my calm baking and botanical gardening era.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Sneaky Link", "p": "/ˈsniːki lɪŋk/", "t": "noun", "m": "A secret romantic hookup or connection kept hidden from mutual friends.", "e": "They kept their weekend meetups completely secret as a sneaky link.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Out Of Pocket", "p": "/aʊt əv ˈpɒkɪt/", "t": "phrase", "m": "Wildly inappropriate, uncalled for, out of line, or unhinged in conduct.", "e": "Bringing up his embarrassing high school haircut in front of his date was out of pocket.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Unserious", "p": "/ʌnˈsɪəriəs/", "t": "adjective", "m": "Silly, chaotic, not taking important matters seriously, or laughing inappropriately.", "e": "The group chat was completely unserious when discussing the group project.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Cringe", "p": "/krɪndʒ/", "t": "adjective/noun", "m": "Causing acute embarrassment, awkward discomfort, or second-hand shame.", "e": "Watching someone dance off-beat to serious music was intensely cringe.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "IYKYK", "p": "/aɪ waɪ keɪ waɪ keɪ/", "t": "phrase", "m": "Acronym for 'If You Know You Know', referencing an inside joke or niche knowledge.", "e": "The smell of campus rain before exam week hits differently, IYKYK.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Opp", "p": "/ɒp/", "t": "noun", "m": "Short for opponent, adversary, or rival.", "e": "My alarm clock set for 6:00 AM is my primary opp in life.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Fit Check", "p": "/fɪt tʃɛk/", "t": "noun", "m": "The inspection or sharing of an outfit on camera to display clothing style.", "e": "Let's do a quick mirror fit check before we leave for the downtown concert.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Put Me On", "p": "/pʊt miː ɒn/", "t": "phrase", "m": "To introduce someone to a great new artist, food, product, or trend.", "e": "Can you put me on to some relaxing jazz hip-hop playlists for studying?", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Baddie", "p": "/ˈbædi/", "t": "noun", "m": "An exceptionally attractive, stylish, confident, and self-assured individual.", "e": "She stepped onto the stage looking like an absolute baddie in that metallic outfit.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Main Character Energy", "p": "/meɪn ˈkærɪktər ˈɛnərdʒi/", "t": "phrase", "m": "Exuding charisma and presence that draws all attention effortlessly.", "e": "Walking through the library with dramatic overcoat flourishes gives main character energy.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Pick-Me", "p": "/pɪk miː/", "t": "noun", "m": "Someone who claims 'they are not like other people' just to seek approval.", "e": "Saying you hate flowers and gifts to look easygoing gave off pick-me behavior.", "c": "Gen Z terms", "d": "Intermediate"},
    {"w": "Rent Free", "p": "/rɛnt friː/", "t": "phrase", "m": "Holding space in someone's mind obsessively and persistently.", "e": "That weird commercial jingle has lived rent free in my memory since 2018.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Valid Take", "p": "/ˈvælɪd teɪk/", "t": "noun", "m": "A well-reasoned, fair, and justifiable opinion on a disputed topic.", "e": "Saying that classic cartoons had better soundtracks is a completely valid take.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "I'm Weak", "p": "/aɪm wiːk/", "t": "phrase", "m": "Overcome with uncontrollable laughter, equivalent to 'I am dying of laughter'.", "e": "That blooper reel where he fell into the foam pit had me weak.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Sending Me", "p": "/ˈsɛndɪŋ miː/", "t": "phrase", "m": "Causing immense amusement, laughter, or hysterical entertainment.", "e": "The cat's confused face after hearing thunder is totally sending me.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Sheesh", "p": "/ʃiːʃ/", "t": "interjection", "m": "High-pitched exclamation of disbelief, awe, or enthusiastic praise.", "e": "Look at those custom sneakers, sheesh!", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Thirsty", "p": "/ˈθɜːrsti/", "t": "adjective", "m": "Desperate for validation, romantic attention, or online likes.", "e": "Leaving comments on ten selfies within three minutes looks a bit thirsty.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Snack", "p": "/snæk/", "t": "noun", "m": "A person who looks noticeably attractive, cute, or visually appetizing.", "e": "He put on that tailored navy suit and looked like a whole snack.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Vibe", "p": "/vaɪb/", "t": "noun/verb", "m": "The emotional atmosphere, tone, mood, or to relax harmoniously.", "e": "This sunny rooftop terrace has the most calming summer vibe.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "W", "p": "/ˈdʌbəl.juː/", "t": "noun", "m": "A win, success, or favorable positive outcome.", "e": "Finding twenty dollars in an old winter coat pocket is an instant W.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "L", "p": "/ɛl/", "t": "noun", "m": "A loss, mistake, defeat, or unfortunate situation.", "e": "Forgetting to save your code before the laptop battery died was a massive L.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Karen", "p": "/ˈkærən/", "t": "noun", "m": "An entitled, demanding person who causes public scenes over trivial grievances.", "e": "She demanded to speak to the restaurant owner because her water had ice.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Fire", "p": "/ˈfaɪər/", "t": "adjective", "m": "Extremely good, impressive, stylish, or high quality.", "e": "The graphic design on that album cover is straight fire.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Finsta", "p": "/ˈfɪnstə/", "t": "noun", "m": "A fake or private Instagram account used to post candid uncurated content.", "e": "She only shares silly memes and unfiltered diaries on her finsta.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Goat Tier", "p": "/ɡoʊt tɪər/", "t": "adjective", "m": "Ranking among the highest, most legendary levels of greatness.", "e": "Grandma's homemade warm apple pie is in goat tier territory.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "IJBOL", "p": "/ɪdʒˈbɒl/", "t": "phrase", "m": "Acronym for 'I Just Burst Out Laughing', representing spontaneous laughter.", "e": "Seeing him try to skate backwards made me ijbol in front of everyone.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Menty B", "p": "/ˈmɛnti biː/", "t": "noun", "m": "Playful slang shorthand for a mental breakdown or emotional overwhelm.", "e": "I had a tiny menty b after spilling iced coffee on my laptop keyboard.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "NPC Energy", "p": "/ˌɛn.piːˈsiː ˈɛnərdʒi/", "t": "phrase", "m": "Dull, generic, or robotic behavior that mirrors automated game background characters.", "e": "His monotone small talk gave off distinct NPC energy.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Real Ones", "p": "/rɪəl wʌnz/", "t": "noun", "m": "Loyal, authentic friends who support you through thick and thin.", "e": "Only the real ones showed up at 6 AM to help me move furniture.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Core", "p": "/kɔːr/", "t": "suffix/noun", "m": "A suffix describing a specific micro-aesthetic or trend subculture (e.g. cottagecore).", "e": "Her cozy wool sweaters and tea mugs are classic bookworm core.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Tea Spill", "p": "/tiː spɪl/", "t": "noun", "m": "The act of revealing drama or secret revelations to an eager audience.", "e": "Last night's podcast was a massive two-hour tea spill.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Vibe Killer", "p": "/vaɪb ˈkɪlər/", "t": "noun", "m": "Someone who ruins a cheerful, energetic, or positive gathering with negativity.", "e": "Bringing up unpaid utility bills during a birthday dinner is a total vibe killer.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Big Yikes", "p": "/bɪɡ jaɪks/", "t": "phrase", "m": "An exclamation of profound embarrassment, cringe, or awkward discomfort.", "e": "He waved back at someone who was waving to the person behind him; big yikes.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Rage Quit", "p": "/reɪdʒ kwɪt/", "t": "verb", "m": "To abandon an activity or video game in an abrupt outburst of frustration.", "e": "After failing the platform jump six times, he rage quit for the evening.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Living It Up", "p": "/ˈlɪvɪŋ ɪt ʌp/", "t": "phrase", "m": "Enjoying life to the fullest with celebration, comfort, and good spirits.", "e": "They spent the holiday weekend lounging on the beach, truly living it up.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Hits The Spot", "p": "/hɪts ðə spɒt/", "t": "phrase", "m": "Provides exactly the satisfaction, refreshment, or comfort required.", "e": "A warm bowl of tomato soup hits the spot on a chilly rainy evening.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "No Cap Zone", "p": "/noʊ kæp zoʊn/", "t": "phrase", "m": "A space or discussion dedicated entirely to genuine truth and zero deceit.", "e": "We entered a strictly no cap zone to discuss our true team goals.", "c": "Gen Z terms", "d": "Easy"},
    {"w": "Valid Point", "p": "/ˈvælɪd pɔɪnt/", "t": "noun", "m": "A strong, rational, and persuasive argument that deserves respect.", "e": "She made a very valid point regarding the budget surplus.", "c": "Gen Z terms", "d": "Easy"}
]

print(f"Gen Z items count: {len(gen_z_dataset)}")

# 3. Categorization and Difficulty reassignment
updated_words = []
existing_names = set()

# First add all Gen Z terms
for item in gen_z_dataset:
    w_lower = item['w'].lower().strip()
    existing_names.add(w_lower)
    updated_words.append(item)

# Count trackers for categories
category_counts = {
    'General English': 0,
    'Gen Z terms': len(gen_z_dataset),
    'Technical terms': 0,
    'Medical terms': 0,
    'Doctor & Specialized': 0,
    'Business terms': 0,
    'Startup terms': 0,
    'History terms': 0
}

difficulty_counts = {'Easy': 0, 'Intermediate': 0, 'Hard': 0}

for item in raw_words:
    w = item.get('w', '').strip()
    w_lower = w.lower()
    if not w or w_lower in existing_names:
        continue
    existing_names.add(w_lower)

    m = item.get('m', '').strip()
    p = item.get('p', '').strip()
    t = item.get('t', '').strip()
    e = item.get('e', '').strip()
    text = f"{w_lower} {m.lower()} {t.lower()}"

    # Determine Category
    # Priority: Doctor -> Medical -> Startup -> Business -> Technical -> History -> General English
    cat = 'General English'
    if any(k in text for k in doctor_keywords):
        cat = 'Doctor & Specialized'
    elif any(k in text for k in medical_keywords):
        cat = 'Medical terms'
    elif any(k in text for k in startup_keywords):
        cat = 'Startup terms'
    elif any(k in text for k in business_keywords):
        cat = 'Business terms'
    elif any(k in text for k in tech_keywords):
        cat = 'Technical terms'
    elif any(k in text for k in history_keywords):
        cat = 'History terms'
    else:
        cat = 'General English'

    # Determine Difficulty
    # If explicitly in easy_force_words -> Easy
    # If word ends with 'ly' and root is familiar -> Easy
    # If in hard_words -> Hard
    # Otherwise: length / syllable / vocabulary check
    diff = 'Intermediate'
    if w_lower in easy_force_words:
        diff = 'Easy'
    elif w_lower in hard_words:
        diff = 'Hard'
    elif w_lower.endswith('ly') and len(w_lower) < 13:
        diff = 'Easy'
    elif len(w_lower) <= 6:
        diff = 'Easy'
    elif len(w_lower) >= 12 and cat in ['Doctor & Specialized', 'Medical terms', 'Technical terms']:
        diff = 'Hard'
    else:
        # Default based on familiarity
        diff = 'Intermediate'

    category_counts[cat] = category_counts.get(cat, 0) + 1
    difficulty_counts[diff] = difficulty_counts.get(diff, 0) + 1

    updated_words.append({
        'w': w,
        'p': p,
        't': t,
        'm': m,
        'e': e,
        'c': cat,
        'd': diff
    })

for d in [w['d'] for w in gen_z_dataset]:
    difficulty_counts[d] = difficulty_counts.get(d, 0) + 1

print("\n--- CATEGORY COUNTS ---")
for k, v in category_counts.items():
    print(f"  {k}: {v}")

print("\n--- DIFFICULTY COUNTS ---")
for k, v in difficulty_counts.items():
    print(f"  {k}: {v}")

# Verify 'Accordingly'
for w in updated_words:
    if w['w'].lower() == 'accordingly':
        print(f"\nVerification of 'Accordingly': Category={w['c']}, Difficulty={w['d']}")
        break

# Write to file
with open('app/src/main/assets/oxford_words.json', 'w', encoding='utf-8') as f:
    json.dump(updated_words, f, separators=(',', ':'))

print(f"\nSuccessfully saved {len(updated_words)} words to app/src/main/assets/oxford_words.json!")
