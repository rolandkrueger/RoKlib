package info.rolandkrueger.roklib.demo.ui.augmentedtypingdemo;

import info.rolandkrueger.roklib.ui.swing.augmentedtyping.AugmentedTypingKeyMapping;

public class GermanKeyMapping extends AugmentedTypingKeyMapping
{
  public GermanKeyMapping ()
  {
    addMapping ('u', new Character[] {'\u00FC'});
    addMapping ('U', new Character[] {'\u00DC'});
    addMapping ('a', new Character[] {'\u00E4'});
    addMapping ('A', new Character[] {'\u00C4'});
    addMapping ('o', new Character[] {'\u00F6'});
    addMapping ('O', new Character[] {'\u00D6'});
    addMapping ('s', new Character[] {'\u00DF'});
    addMapping ('b', new Character[] {'\u00DF'});
    addMapping ('B', new Character[] {'\u00DF'});
  }
}
