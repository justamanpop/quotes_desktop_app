package quotes

import Quote
import Tag
import ports.driven.QuoteRepository
import tags.fakeTags

class InMemoryQuoteRepository : QuoteRepository {
    override fun getQuotes(): List<Quote> {
        return fakeQuotes
    }

    override fun addQuote(quote: Quote) {
        val quoteToAdd = quote.copy(id = maxId + 1)
        fakeQuotes += quoteToAdd
        maxId += 1
    }

    override fun addQuotes(quotes: List<Quote>) {
        quotes.forEach { quote ->
            addQuote(quote)
        }
    }

    override fun deleteQuote(quoteId: Int) {
        fakeQuotes = fakeQuotes.filterNot { q -> q.id == quoteId }
    }

    private var fakeQuotes = listOf<Quote>(
        Quote(
            1, "No price is too great to pay for the privilege of owning yourself", "Friedrich Nietzsche",
            listOf(fakeTags.get("Nietzsche"), fakeTags.get("Congruence")) as List<Tag>
        ),
        Quote(
            2,
            "If we are to live, we must take risks. Else our lives become deaths in all but name. There is no struggle too vast, no odds too overwhelming, for even should we fail, should we fall, we will know that we have lived.",
            "Anomander Rake",
            listOf(
                fakeTags.get("Malazan"),
                fakeTags.get("Inspirational"),
                fakeTags.get("Defiance"),
                fakeTags.get("Purpose")
            ) as List<Tag>
        ),
        Quote(
            3,
            "This above all: to thine own self be true, And it must follow, as the night the day, Thou canst not then be false to any man.",
            "William Shakespeare",
            listOf(fakeTags.get("Shakespeare"), fakeTags.get("Congruence")) as List<Tag>
        ),
        Quote(
            4,
            "All the world's a stage,And all the men and women merely players",
            "William Shakespeare",
            listOf(fakeTags.get("Shakespeare"), fakeTags.get("No one cares")) as List<Tag>
        ),
        Quote(
            5,
            "“There is no such thing as a good influence, Mr. Gray. All influence is immoral—immoral from the scientific point of view.”" +
                    "\n\n" +
                    "“Why?”" +
                    "\n\n" +
                    "“Because to influence a person is to give him one’s own soul. He does not think his natural thoughts, or burn with his natural passions. His virtues are not real to him. His sins, if there are such things as sins, are borrowed. He becomes an echo of some one else’s music, an actor of a part that has not been written for him. The aim of life is self-development. To realize one’s nature perfectly—that is what each of us is here for. People are afraid of themselves, nowadays. They have forgotten the highest of all duties, the duty that one owes to one’s self. Of course, they are charitable. They feed the hungry and clothe the beggar. But their own souls starve, and are naked. Courage has gone out of our race. Perhaps we never really had it. The terror of society, which is the basis of morals, the terror of God, which is the secret of religion—these are the two things that govern us.",
            "The Picture of Dorian Grey - Henry Wotton",
            listOf(fakeTags.get("Dorian Grey"), fakeTags.get("Congruence")) as List<Tag>
        ),
        Quote(
            6,
            "“... I thought how tragic it would be if you were wasted. For there is such a little time that your youth will last — such a little time. The common hill-flowers wither, but they blossom again. The laburnum will be as yellow next June as it is now. In a month there will be purple stars on the clematis, and year after year the green night of its leaves will hold its purple stars. But we never get back our youth. The pulse of joy that beats in us at twenty becomes sluggish. Our limbs fail, our senses rot. We degenerate into hideous puppets, haunted by the memory of the passions of which we were too much afraid, and the exquisite temptations that we had not the courage to yield to. Youth! Youth! There is absolutely nothing in the world but youth!”",
            "The Picture of Dorian Grey - Henry Wotton",
            listOf(fakeTags.get("Dorian Grey"), fakeTags.get("Congruence")) as List<Tag>
        ),
        Quote(
            7,
            "“It’s the ignorant who find a cause and cling to it, for within that is the illusion of significance.”",
            "Deadhouse Gates - Steven Erikson",
            listOf(fakeTags.get("Malazan"), fakeTags.get("Purpose")) as List<Tag>
        ),
        Quote(
            8,
            "Ambition is not a dirty word. Piss on compromise. Go for the throat.",
            "Gardens of the Moon - Steven Erikson",
            listOf(fakeTags.get("Malazan"), fakeTags.get("Inspirational")) as List<Tag>
        ),
        Quote(
            9,
            "It is better to be hated for what you are than to be loved for what you are not",
            "Autumn Leaves - Andre Gide",
            listOf(fakeTags.get("Congruence"), fakeTags.get("Inspirational")) as List<Tag>
        ),
        Quote(
            10,
            "Develop interest in life as you see it; in people, things, literature, music—the world is so rich, simply throbbing with rich treasures, beautiful souls and interesting people. Forget yourself.",
            "Henry Miller",
            listOf(fakeTags.get("Beauty")) as List<Tag>
        )
    )
    private var maxId = fakeQuotes.size
}