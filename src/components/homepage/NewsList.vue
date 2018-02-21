<template>
  <v-ons-page>
    <v-ons-toolbar>
      <div class="left">
        <v-ons-toolbar-button @click="$emit('toggleSetting')">
          <v-ons-icon icon="ion-navicon, material:md-menu"></v-ons-icon>
        </v-ons-toolbar-button>
      </div>
      <div class="center">{{ msg }}</div>
    </v-ons-toolbar>

    <v-ons-pull-hook
      :action="update_news"
      @changestate="pullState = $event.state"
    >
      <span v-show="pullState === 'initial'"> 下拉刷新 </span>
      <span v-show="pullState === 'preaction'"> 松开刷新 </span>
      <span v-show="pullState === 'action'"> 加载中... </span>
    </v-ons-pull-hook>

    <v-ons-list>
      <v-ons-list-item modifier="chevron" tappable v-for="item in news" @click="$emit('open-news', item)" :key="item.url">
        <div class="left" v-show="item.thumbnail != null">
          <img class="list-item__thumbnail" :src="item.thumbnail"/>
          </div>
        <div class="center">
          <span class="list-item__title"> {{ item.title }} </span>
          <span class="list-item__subtitle"> {{ item.date }} </span>
        </div>
      </v-ons-list-item>
    </v-ons-list>
  </v-ons-page>
</template>

<script>
export default {
  data() {
    return {
      msg: "炎陵新闻",
      pullState: 'initial',
      news: []
    }
  },
  methods: {
    update_news(done) {
      scraper.fetchNewsList().then(x => {
        this.news = x
      }).then(done).catch(done)
    }
  }
}
</script>
