package de.digitaldealer.cardsplease.data.repositories

class DeckRepository : BaseRepository() {

    suspend fun getNewDeck() = apiCall { api.getNewDeck() }

    suspend fun shuffleDeck(deckId: String) = apiCall { api.shuffleDeck(deckId) }

    suspend fun drawAmountOfCards(amount: Int, deckId: String) = apiCall { api.drawAmountOfCards(deckId = deckId, count = amount) }

//    private val questionDao by inject<QuestionDao>()

//    suspend fun startDownload(): ResponseEvaluator.Result<QuestionListDto> = apiCall { api.loadQuestions("android") }

//    fun watchAllQuestions(): Flow<List<QuestionEntity>> = questionDao.watchAll()
//
//    fun saveQuestions(questionListDto: QuestionListDto) {
//        repositoryScope.launch {
//            questionDao.insert(questionListDto.toQuestionEntityList())
//        }
//    }
}