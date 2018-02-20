<template>
  <v-ons-navigator swipeable :page-stack="pageStack" @open-news="openNews($event)" />
</template>

<script>
import HomePage from './components/HomePage'
import NewsPage from './components/NewsPage'

console.log(HomePage)

export default {
  data () {
    return {
      pageStack: [HomePage]
    }
  },
  methods: {
    openNews(news) {
      this.pageStack.push({
        ...NewsPage,
        onsNavigatorProps: news
      })
    }
  }
}
</script>
